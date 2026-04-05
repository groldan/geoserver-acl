/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 *
 * Original from GeoFence 3.6 under GPL 2.0 license
 */

package org.geoserver.acl.domain.adminrules;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.geoserver.acl.domain.filter.RuleQuery;

/**
 * Default implementation of {@link AdminRuleAdminService}.
 *
 * <p>Manages workspace admin rules with thread-safe priority operations and event publishing for
 * rule changes.
 *
 * @author Emanuele Tajariol (etj at geo-solutions.it) (originally as part of GeoFence)
 * @author Gabriel Roldan (adapted to GeoServer ACL)
 */
@RequiredArgsConstructor
public class AdminRuleAdminServiceImpl implements AdminRuleAdminService {

    /**
     * Lock to serialize all priority-related operations.
     * Acquired for operations that modify priorities (insert, update, shift, swap).
     * This prevents race conditions when multiple threads try to create/update rules
     * with the same priority. The lock is acquired BEFORE starting the transaction.
     *
     * Note: Instance variable (not static) so each Spring context has its own lock.
     */
    private final ReentrantLock priorityLock = new ReentrantLock();

    private final @NonNull AdminRuleRepository repository;

    @Setter
    private @NonNull Consumer<AdminRuleEvent> eventPublisher = r -> {
        // no-op
    };

    // =========================================================================
    // Basic operations
    // =========================================================================

    @Override
    public AdminRule insert(AdminRule rule) {
        return insert(rule, InsertPosition.FIXED);
    }

    @Override
    public AdminRule insert(AdminRule rule, InsertPosition position) {
        // Acquire lock BEFORE the transaction starts to prevent race conditions
        priorityLock.lock();
        try {
            AdminRule created = repository.create(rule, position);
            eventPublisher.accept(AdminRuleEvent.created(created));
            return created;
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public AdminRule update(AdminRule rule) {
        // Acquire lock since updating can shift priorities
        priorityLock.lock();
        try {
            if (null == rule.getId()) {
                throw new IllegalArgumentException("AdminRule has no id");
            }

            AdminRule updated = repository.save(rule);
            eventPublisher.accept(AdminRuleEvent.updated(updated));
            return updated;
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public int shift(long priorityStart, long offset) {
        // Acquire lock for explicit priority shifting
        priorityLock.lock();
        try {
            return repository.shiftPriority(priorityStart, offset);
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public void swap(@NonNull String id1, @NonNull String id2) {
        // Acquire lock for swapping priorities
        priorityLock.lock();
        try {
            repository.swap(id1, id2);
            eventPublisher.accept(AdminRuleEvent.updated(id1, id2));
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public Optional<AdminRule> get(@NonNull String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<AdminRule> getFirstMatch(AdminRuleFilter filter) {
        return repository.findFirst(filter);
    }

    @Override
    public boolean delete(@NonNull String id) {
        boolean deleted = repository.deleteById(id);
        if (deleted) eventPublisher.accept(AdminRuleEvent.deleted(id));
        return deleted;
    }

    @Override
    public int deleteAll() {
        return repository.deleteAll();
    }

    @Override
    public Stream<AdminRule> getAll() {
        return repository.findAll();
    }

    @Override
    public Stream<AdminRule> getAll(RuleQuery<AdminRuleFilter> query) {
        return repository.findAll(query);
    }

    @Override
    public Optional<AdminRule> getRuleByPriority(long priority) {
        return repository.findOneByPriority(priority);
    }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public int count(AdminRuleFilter filter) {
        return repository.count(filter);
    }

    @Override
    public boolean exists(@NonNull String id) {
        return repository.findById(id).isPresent();
    }
}
