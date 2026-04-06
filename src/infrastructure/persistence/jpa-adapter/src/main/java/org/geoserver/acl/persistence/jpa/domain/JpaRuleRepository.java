/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.persistence.jpa.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

@TransactionSupported
public interface JpaRuleRepository
        extends JpaRepository<JpaRule, Long>, QuerydslPredicateExecutor<JpaRule>, PriorityRepository<JpaRule> {

    @Override
    Optional<JpaRule> findOneByPriority(long priority);

    @Override
    @TransactionRequired
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(
            value =
                    "UPDATE {h-schema}acl_rule_priority SET priority = priority + :offset WHERE priority >= :priorityStart",
            nativeQuery = true)
    int shiftPriority(@Param("priorityStart") long priorityStart, @Param("offset") long offset);

    @Override
    @TransactionRequired
    @Query("SELECT r.id FROM Rule r WHERE priority >= :priorityStart")
    Stream<Long> streamIdsByShiftPriority(@Param("priorityStart") long priorityStart);

    @Override
    @TransactionRequired
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(
            value =
                    "UPDATE {h-schema}acl_rule_priority SET priority = priority + :offset WHERE priority BETWEEN :min AND :max",
            nativeQuery = true)
    void shiftPrioritiesBetween(@Param("min") long min, @Param("max") long max, @Param("offset") long offset);

    @Override
    @TransactionRequired
    @Query("SELECT r.id FROM Rule r WHERE priority BETWEEN :min AND :max")
    Stream<Long> streamIdsByShiftPriorityBetween(@Param("min") long min, @Param("max") long max);

    @Override
    @Query("SELECT MAX(r.priority) FROM Rule r")
    Optional<Long> findMaxPriority();

    @Override
    @Query("SELECT MIN(r.priority) FROM Rule r")
    Optional<Long> findMinPriority();

    List<JpaRule> findAllByIdentifier(JpaRuleIdentifier identifier);

    @Override
    @TransactionRequired
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(
            value = "UPDATE {h-schema}acl_rule_priority SET priority = CASE"
                    + " WHEN id = :id1 THEN :p2"
                    + " WHEN id = :id2 THEN :p1 END"
                    + " WHERE id IN (:id1, :id2)",
            nativeQuery = true)
    void swapPriorities(
            @Param("id1") long id1, @Param("p1") long priority1, @Param("id2") long id2, @Param("p2") long priority2);
}
