/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 *
 * Original from GeoFence 3.6 under GPL 2.0 license
 */

package org.geoserver.acl.domain.rules;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.geoserver.acl.domain.filter.RuleQuery;

/**
 * Domain service for managing data access {@link Rule}s.
 *
 * <p>Provides CRUD operations and queries for rules that control access to GeoServer layers and
 * workspaces. Rules are maintained in priority order, where lower priority values take precedence.
 *
 * <p>Service and request names in rule identifiers are normalized to uppercase per OGC
 * specifications, which treat them as case-insensitive.
 *
 * @author Emanuele Tajariol (etj at geo-solutions.it) (originally as part of GeoFence)
 * @author Gabriel Roldan - Camptocamp
 */
@RequiredArgsConstructor
public class RuleAdminServiceImpl implements RuleAdminService {

    /**
     * Lock to serialize all priority-related operations.
     * Acquired for operations that modify priorities (insert, update, shift, swap).
     * This prevents race conditions when multiple threads try to create/update rules
     * with the same priority. The lock is acquired BEFORE starting the transaction.
     *
     * Note: Instance variable (not static) so each Spring context has its own lock.
     */
    private final ReentrantLock priorityLock = new ReentrantLock();

    private final @NonNull RuleRepository ruleRepository;

    @Setter
    private @NonNull Consumer<RuleEvent> eventPublisher = r -> {
        // no-op
    };

    // =========================================================================
    // Basic operations
    // =========================================================================

    @Override
    public Rule insert(@NonNull Rule rule) {
        return insert(rule, InsertPosition.FIXED);
    }

    @Override
    public Rule insert(@NonNull Rule rule, @NonNull InsertPosition position) {
        // Acquire lock BEFORE the transaction starts to prevent race conditions
        priorityLock.lock();
        try {
            if (null != rule.getId())
                throw new IllegalArgumentException("a new Rule must not have id, got " + rule.getId());

            rule = sanitizeFields(rule);
            Rule created = ruleRepository.create(rule, position);

            eventPublisher.accept(RuleEvent.created(created));
            return created;
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public Rule update(@NonNull Rule rule) {
        // Acquire lock since updating can shift priorities
        priorityLock.lock();
        try {
            if (null == rule.getId()) {
                throw new IllegalArgumentException("Rule has no id");
            }

            rule = sanitizeFields(rule);
            Rule updated = ruleRepository.save(rule);
            eventPublisher.accept(RuleEvent.updated(updated));
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
            if (offset <= 0) {
                throw new IllegalArgumentException("Positive offset required");
            }
            return ruleRepository.shift(priorityStart, offset);
        } finally {
            priorityLock.unlock();
        }
    }

    @Override
    public void swapPriority(String id1, String id2) {
        // Acquire lock for swapping priorities
        priorityLock.lock();
        try {
            ruleRepository.swap(id1, id2);
            eventPublisher.accept(RuleEvent.updated(id1, id2));
        } finally {
            priorityLock.unlock();
        }
    }

    /**
     * {@code service} and {@code request} params are usually set by the client, and by OGC specs
     * they are not case sensitive, so we're going to turn all of them uppercase. See also {@link
     * AuthorizationService}.
     */
    protected Rule sanitizeFields(Rule rule) {
        if (rule.getPriority() < 0)
            throw new IllegalArgumentException("Negative priority is not allowed: " + rule.getPriority());

        // read class' javadoc
        RuleIdentifier identifier = rule.getIdentifier();
        if (identifier.getService() != null) {
            identifier = identifier.withService(identifier.getService().toUpperCase());
        }
        if (identifier.getRequest() != null) {
            identifier = identifier.withRequest(identifier.getRequest().toUpperCase());
        }
        return rule.withIdentifier(identifier);
    }

    @Override
    public Optional<Rule> get(@NonNull String id) {
        return ruleRepository.findById(id);
    }

    @Override
    public boolean delete(@NonNull String id) {
        boolean deleted = ruleRepository.deleteById(id);
        if (deleted) {
            eventPublisher.accept(RuleEvent.deleted(id));
        }
        return deleted;
    }

    @Override
    public int deleteAll() {
        return ruleRepository.deleteAll();
    }

    @Override
    public Stream<Rule> getAll() {
        return ruleRepository.findAll();
    }

    @Override
    public Stream<Rule> getAll(@NonNull RuleQuery<RuleFilter> query) {
        return ruleRepository.findAll(query);
    }

    @Override
    public Optional<Rule> getRule(@NonNull RuleFilter filter) throws IllegalArgumentException {
        RuleQuery<RuleFilter> query = RuleQuery.of(filter).setLimit(2);
        List<Rule> found = ruleRepository.findAll(query).collect(Collectors.toList());
        if (found.size() > 1) {
            throw new IllegalArgumentException("Unexpected rule count for filter " + filter + " : " + found.size());
        }

        return Optional.ofNullable(found.isEmpty() ? null : found.get(0));
    }

    @Override
    public Optional<Rule> getRuleByPriority(long priority) throws IllegalArgumentException {
        return ruleRepository.findOneByPriority(priority);
    }

    @Override
    public int count() {
        return ruleRepository.count();
    }

    @Override
    public int count(@NonNull RuleFilter filter) {
        return ruleRepository.count(filter);
    }

    // =========================================================================
    // Limits
    // =========================================================================

    @Override
    public void setLimits(@NonNull String ruleId, RuleLimits limits) throws IllegalArgumentException {

        ruleRepository.setLimits(ruleId, limits);
        eventPublisher.accept(RuleEvent.updated(ruleId));
    }

    // =========================================================================
    // Details
    // =========================================================================

    @Override
    public Optional<LayerDetails> getLayerDetails(@NonNull Rule rule) {
        Objects.requireNonNull(rule.getId());
        return getLayerDetails(rule.getId());
    }

    @Override
    public Optional<LayerDetails> getLayerDetails(@NonNull String ruleId) {
        return ruleRepository.findLayerDetailsByRuleId(ruleId);
    }

    @Override
    public void setLayerDetails(@NonNull String ruleId, LayerDetails detailsNew) {
        ruleRepository.setLayerDetails(ruleId, detailsNew);
        eventPublisher.accept(RuleEvent.updated(ruleId));
    }

    @Override
    public void setAllowedStyles(@NonNull String ruleId, Set<String> styles) {
        ruleRepository.setAllowedStyles(ruleId, styles);
        eventPublisher.accept(RuleEvent.updated(ruleId));
    }

    @Override
    public Set<String> getAllowedStyles(@NonNull String ruleId) {
        return getLayerDetails(ruleId).map(LayerDetails::getAllowedStyles).orElse(Set.of());
    }

    // ==========================================================================

}
