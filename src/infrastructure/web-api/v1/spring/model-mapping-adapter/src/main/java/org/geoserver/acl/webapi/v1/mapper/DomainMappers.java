/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.webapi.v1.mapper;

import lombok.experimental.UtilityClass;

@UtilityClass
class DomainMappers {

    private RuleFilterApiMapper ruleFilterApiMapper;
    private AdminRuleApiMapperImpl adminRuleApiMapper;
    private AuthorizationModelApiMapper authorizationApiMapper;
    private EnumsApiMapperImpl enumsApiMapper;
    private RuleApiMapperImpl ruleApiMapper;
    private RuleLimitsApiMapperImpl ruleLimitsApiMapper;
    private LayerDetailsApiMapperImpl layerDetailsApiMapper;
    private GeometryApiMapperImpl geometryApiMapper;

    public RuleFilterApiMapper ruleFilterApiMapper() {
        if (null == ruleFilterApiMapper) ruleFilterApiMapper = new RuleFilterApiMapper();
        return ruleFilterApiMapper;
    }

    public AdminRuleApiMapper adminRuleApiMapper() {
        if (null == adminRuleApiMapper) adminRuleApiMapper = new AdminRuleApiMapperImpl(enumsApiMapper());
        return adminRuleApiMapper;
    }

    public AuthorizationModelApiMapper authorizationApiMapper() {
        if (null == authorizationApiMapper) {
            authorizationApiMapper = new AuthorizationModelApiMapperImpl(geometryMapper());
        }
        return authorizationApiMapper;
    }

    public EnumsApiMapper enumsApiMapper() {
        if (null == enumsApiMapper) enumsApiMapper = new EnumsApiMapperImpl();
        return enumsApiMapper;
    }

    public RuleApiMapper ruleApiMapper() {
        if (null == ruleApiMapper) ruleApiMapper = new RuleApiMapperImpl(enumsApiMapper(), ruleLimitsApiMapper());
        return ruleApiMapper;
    }

    public RuleLimitsApiMapper ruleLimitsApiMapper() {
        if (null == ruleLimitsApiMapper)
            ruleLimitsApiMapper = new RuleLimitsApiMapperImpl(geometryMapper(), enumsApiMapper());
        return ruleLimitsApiMapper;
    }

    public LayerDetailsApiMapper layerDetailsApiMapper() {
        if (null == layerDetailsApiMapper)
            layerDetailsApiMapper = new LayerDetailsApiMapperImpl(
                    geometryMapper(), new LayerAttributeApiMapperImpl(enumsApiMapper()), enumsApiMapper());
        return layerDetailsApiMapper;
    }

    public GeometryApiMapperImpl geometryMapper() {
        if (null == geometryApiMapper) geometryApiMapper = new GeometryApiMapperImpl();
        return geometryApiMapper;
    }
}
