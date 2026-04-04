/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.webapi.v1.server;

import lombok.NonNull;
import org.geoserver.acl.domain.rules.LayerDetails;
import org.geoserver.acl.domain.rules.RuleLimits;
import org.geoserver.acl.webapi.v1.model.Rule;
import org.springframework.web.context.request.NativeWebRequest;

public class DataRulesApiSupport extends ApiImplSupport<Rule, org.geoserver.acl.domain.rules.Rule> {

    public DataRulesApiSupport(@NonNull NativeWebRequest nativeRequest) {

        super(nativeRequest);
    }

    @Override
    public org.geoserver.acl.domain.rules.Rule toModel(Rule dto) {
        return apiModelMapper.toModel(dto);
    }

    @Override
    public Rule toApi(org.geoserver.acl.domain.rules.Rule model) {
        return apiModelMapper.toApi(model);
    }

    public LayerDetails toModel(org.geoserver.acl.webapi.v1.model.LayerDetails layerDetails) {
        return apiModelMapper.toModel(layerDetails);
    }

    public org.geoserver.acl.webapi.v1.model.LayerDetails toApi(LayerDetails dto) {
        return apiModelMapper.toApi(dto);
    }

    public RuleLimits toModel(org.geoserver.acl.webapi.v1.model.RuleLimits ruleLimits) {
        return apiModelMapper.toModel(ruleLimits);
    }
}
