/* (c) 2026  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.webapi.v1.mapper;

import org.geoserver.acl.authorization.AccessInfo;
import org.geoserver.acl.authorization.AccessRequest;
import org.geoserver.acl.authorization.AccessSummaryRequest;
import org.geoserver.acl.authorization.AdminAccessRequest;
import org.geoserver.acl.domain.adminrules.AdminRule;
import org.geoserver.acl.domain.adminrules.AdminRuleFilter;
import org.geoserver.acl.domain.rules.LayerDetails;
import org.geoserver.acl.domain.rules.Rule;
import org.geoserver.acl.domain.rules.RuleFilter;
import org.geoserver.acl.domain.rules.RuleLimits;
import org.geoserver.acl.webapi.v1.model.AccessSummary;
import org.geoserver.acl.webapi.v1.model.AdminAccessInfo;

/**
 * Unified api-model mapper interface
 */
public class AclApiModelMapper {

    public void setUseWkb(boolean useWkb) {
        GeometryApiMapper.setUseWkb(useWkb);
    }

    public org.geoserver.acl.webapi.v1.model.InsertPosition toApi(
            org.geoserver.acl.domain.rules.InsertPosition position) {
        return DomainMappers.enumsApiMapper().map(position);
    }

    public org.geoserver.acl.webapi.v1.model.InsertPosition toApi(
            org.geoserver.acl.domain.adminrules.InsertPosition position) {
        return DomainMappers.enumsApiMapper().map(position);
    }

    public org.geoserver.acl.domain.rules.InsertPosition toRuleModel(
            org.geoserver.acl.webapi.v1.model.InsertPosition position) {
        return DomainMappers.enumsApiMapper().toRuleInsertPosition(position);
    }

    public org.geoserver.acl.domain.adminrules.InsertPosition toAdminRuleModel(
            org.geoserver.acl.webapi.v1.model.InsertPosition position) {
        return DomainMappers.enumsApiMapper().toAdminRuleInsertPosition(position);
    }

    public AccessInfo toModel(org.geoserver.acl.webapi.v1.model.AccessInfo dto) {
        return DomainMappers.authorizationApiMapper().toModel(dto);
    }

    public org.geoserver.acl.webapi.v1.model.AccessInfo toApi(AccessInfo model) {
        return DomainMappers.authorizationApiMapper().toApi(model);
    }

    public org.geoserver.acl.webapi.v1.model.AccessRequest toApi(AccessRequest request) {
        return DomainMappers.authorizationApiMapper().toApi(request);
    }

    public AccessRequest toModel(org.geoserver.acl.webapi.v1.model.AccessRequest request) {
        return DomainMappers.authorizationApiMapper().toModel(request);
    }

    public AdminAccessRequest toModel(org.geoserver.acl.webapi.v1.model.AdminAccessRequest request) {
        return DomainMappers.authorizationApiMapper().toModel(request);
    }

    public org.geoserver.acl.webapi.v1.model.AdminAccessRequest toApi(AdminAccessRequest request) {
        return DomainMappers.authorizationApiMapper().toApi(request);
    }

    public AdminAccessInfo toApi(org.geoserver.acl.authorization.AdminAccessInfo response) {
        return DomainMappers.authorizationApiMapper().toApi(response);
    }

    public org.geoserver.acl.authorization.AdminAccessInfo toModel(AdminAccessInfo response) {
        return DomainMappers.authorizationApiMapper().toModel(response);
    }

    public AdminRule toModel(org.geoserver.acl.webapi.v1.model.AdminRule dto) {
        return DomainMappers.adminRuleApiMapper().toModel(dto);
    }

    public org.geoserver.acl.webapi.v1.model.AdminRule toApi(AdminRule model) {
        return DomainMappers.adminRuleApiMapper().toApi(model);
    }

    public Rule toModel(org.geoserver.acl.webapi.v1.model.Rule dto) {
        return DomainMappers.ruleApiMapper().toModel(dto);
    }

    public org.geoserver.acl.webapi.v1.model.Rule toApi(Rule model) {
        return DomainMappers.ruleApiMapper().toApi(model);
    }

    public LayerDetails toModel(org.geoserver.acl.webapi.v1.model.LayerDetails layerDetails) {
        return DomainMappers.layerDetailsApiMapper().map(layerDetails);
    }

    public org.geoserver.acl.webapi.v1.model.LayerDetails toApi(LayerDetails dto) {
        return DomainMappers.layerDetailsApiMapper().map(dto);
    }

    public RuleLimits toModel(org.geoserver.acl.webapi.v1.model.RuleLimits ruleLimits) {
        return DomainMappers.ruleLimitsApiMapper().toModel(ruleLimits);
    }

    public org.geoserver.acl.webapi.v1.model.RuleLimits toApi(RuleLimits ruleLimits) {
        return DomainMappers.ruleLimitsApiMapper().toApi(ruleLimits);
    }

    public AccessSummaryRequest toModel(org.geoserver.acl.webapi.v1.model.AccessSummaryRequest request) {
        return DomainMappers.authorizationApiMapper().toModel(request);
    }

    public org.geoserver.acl.webapi.v1.model.AccessSummaryRequest toApi(AccessSummaryRequest request) {
        return DomainMappers.authorizationApiMapper().toApi(request);
    }

    public AccessSummary toApi(org.geoserver.acl.authorization.AccessSummary modelResponse) {
        return DomainMappers.authorizationApiMapper().toApi(modelResponse);
    }

    public org.geoserver.acl.authorization.AccessSummary toModel(AccessSummary modelResponse) {
        return DomainMappers.authorizationApiMapper().toModel(modelResponse);
    }

    public org.geoserver.acl.webapi.v1.model.AdminRuleFilter toApi(AdminRuleFilter filter) {
        return DomainMappers.ruleFilterApiMapper().map(filter);
    }

    public AdminRuleFilter toModel(org.geoserver.acl.webapi.v1.model.AdminRuleFilter adminRuleFilter) {
        return DomainMappers.ruleFilterApiMapper().map(adminRuleFilter);
    }

    public org.geoserver.acl.webapi.v1.model.RuleFilter toApi(RuleFilter filter) {
        return DomainMappers.ruleFilterApiMapper().toApi(filter);
    }

    public RuleFilter toModel(org.geoserver.acl.webapi.v1.model.RuleFilter filter) {
        return DomainMappers.ruleFilterApiMapper().toModel(filter);
    }
}
