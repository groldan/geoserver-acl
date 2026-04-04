/*
 * Move priority columns to separate tables for efficient bulk priority updates.
 * PostgreSQL rewrites full row tuples on UPDATE (MVCC); isolating the frequently-updated
 * priority column avoids rewriting wide rows with geometry/TOAST data.
 */

-- Create secondary tables
CREATE TABLE acl_rule_priority (
    id BIGINT NOT NULL,
    priority BIGINT NOT NULL,
    CONSTRAINT acl_rule_priority_pk PRIMARY KEY (id),
    CONSTRAINT fk_rule_priority_rule FOREIGN KEY (id) REFERENCES acl_rule(id) ON DELETE CASCADE
);

CREATE TABLE acl_adminrule_priority (
    id BIGINT NOT NULL,
    priority BIGINT NOT NULL,
    CONSTRAINT acl_adminrule_priority_pk PRIMARY KEY (id),
    CONSTRAINT fk_adminrule_priority_adminrule FOREIGN KEY (id) REFERENCES acl_adminrule(id) ON DELETE CASCADE
);

-- Migrate existing data
INSERT INTO acl_rule_priority (id, priority) SELECT id, priority FROM acl_rule;
INSERT INTO acl_adminrule_priority (id, priority) SELECT id, priority FROM acl_adminrule;

-- Drop old indexes, create new ones on secondary tables (reuse names)
DROP INDEX IF EXISTS idx_rule_priority;
DROP INDEX IF EXISTS idx_adminrule_priority;
CREATE INDEX idx_rule_priority ON acl_rule_priority (priority);
CREATE INDEX idx_adminrule_priority ON acl_adminrule_priority (priority);

-- Drop old columns
ALTER TABLE acl_rule DROP COLUMN priority;
ALTER TABLE acl_adminrule DROP COLUMN priority;
