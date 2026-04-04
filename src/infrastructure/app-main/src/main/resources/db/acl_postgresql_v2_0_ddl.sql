create sequence acl_adminrule_sequence start with 1 increment by 1;

create sequence acl_rule_sequence start with 1 increment by 1;

create table acl_adminrule (
    ip_size integer,
    createdDate timestamp(6) not null,
    id bigint not null,
    ip_high bigint,
    ip_low bigint,
    lastModifiedDate timestamp(6),
    priority bigint not null,
    description varchar(4096),
    createdBy varchar(255),
    extId varchar(255) unique,
    grant_type varchar(255) not null check ((grant_type in ('ADMIN','USER'))),
    lastModifiedBy varchar(255),
    name varchar(255),
    rolename varchar(255) not null,
    username varchar(255) not null,
    workspace varchar(255) not null,
    primary key (id),
    unique (username, rolename, workspace, ip_low, ip_high, ip_size)
);

create table acl_layer_attributes (
    details_id bigint not null,
    access_type varchar(255) check ((access_type in ('NONE','READONLY','READWRITE'))),
    data_type varchar(255),
    name varchar(255) not null,
    constraint acl_layer_attributes_name unique (details_id, name)
);

create table acl_layer_styles (
    details_id bigint not null,
    ld_styleName varchar(255)
);

create table acl_rule (
    ip_size integer,
    createdDate timestamp(6) not null,
    id bigint not null,
    ip_high bigint,
    ip_low bigint,
    lastModifiedDate timestamp(6),
    priority bigint not null,
    description varchar(4096),
    ld_cql_filter_read varchar(65535),
    ld_cql_filter_write varchar(65535),
    createdBy varchar(255),
    extId varchar(255) unique,
    grant_type varchar(255) not null check ((grant_type in ('ALLOW','DENY','LIMIT'))),
    lastModifiedBy varchar(255),
    layer varchar(255) not null,
    ld_catalog_mode varchar(255) check ((ld_catalog_mode in ('HIDE','CHALLENGE','MIXED'))),
    ld_default_style varchar(255),
    ld_spatial_filter_type varchar(255) check ((ld_spatial_filter_type in ('INTERSECT','CLIP'))),
    ld_type varchar(255) check ((ld_type in ('VECTOR','RASTER','LAYERGROUP'))),
    limits_catalog_mode varchar(255) check ((limits_catalog_mode in ('HIDE','CHALLENGE','MIXED'))),
    limits_spatial_filter_type varchar(255) check ((limits_spatial_filter_type in ('INTERSECT','CLIP'))),
    name varchar(255),
    request varchar(255) not null,
    rolename varchar(255) not null,
    service varchar(255) not null,
    subfield varchar(255) not null,
    username varchar(255) not null,
    workspace varchar(255) not null,
    ld_area geometry,
    limits_area geometry,
    primary key (id)
);

create index idx_adminrule_priority on acl_adminrule (priority);
create index idx_adminrule_username on acl_adminrule (username);
create index idx_adminrule_rolename on acl_adminrule (rolename);
create index idx_adminrule_workspace on acl_adminrule (workspace);
create index idx_adminrule_grant_type on acl_adminrule (grant_type);
create index idx_rule_priority on acl_rule (priority);
create index idx_rule_service on acl_rule (service);
create index idx_rule_request on acl_rule (request);
create index idx_rule_workspace  on acl_rule (workspace);
create index idx_rule_layer on acl_rule (layer);

alter table if exists acl_layer_attributes
   add constraint fk_attribute_layer
   foreign key (details_id)
   references acl_rule;

alter table if exists acl_layer_styles
   add constraint fk_styles_layer
   foreign key (details_id)
   references acl_rule;
