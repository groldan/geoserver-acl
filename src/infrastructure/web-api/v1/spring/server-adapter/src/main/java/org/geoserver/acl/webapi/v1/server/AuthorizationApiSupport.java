/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.webapi.v1.server;

import lombok.NonNull;
import org.geoserver.acl.authorization.AccessRequest;
import org.geoserver.acl.authorization.AccessSummaryRequest;
import org.geoserver.acl.authorization.AdminAccessRequest;
import org.geoserver.acl.webapi.v1.model.AccessInfo;
import org.geoserver.acl.webapi.v1.model.AccessSummary;
import org.geoserver.acl.webapi.v1.model.AdminAccessInfo;
import org.geoserver.acl.webapi.v1.model.Rule;
import org.springframework.web.context.request.NativeWebRequest;

public class AuthorizationApiSupport extends ApiImplSupport<AccessInfo, org.geoserver.acl.authorization.AccessInfo> {

    public AuthorizationApiSupport(@NonNull NativeWebRequest nativeRequest) {
        super(nativeRequest);
    }

    @Override
    public org.geoserver.acl.authorization.AccessInfo toModel(AccessInfo dto) {
        return apiModelMapper.toModel(dto);
    }

    @Override
    public AccessInfo toApi(org.geoserver.acl.authorization.AccessInfo model) {
        return apiModelMapper.toApi(model);
    }

    public AccessRequest toModel(org.geoserver.acl.webapi.v1.model.AccessRequest request) {
        return apiModelMapper.toModel(request);
    }

    public AdminAccessRequest toModel(org.geoserver.acl.webapi.v1.model.AdminAccessRequest request) {
        return apiModelMapper.toModel(request);
    }

    public AdminAccessInfo toApi(org.geoserver.acl.authorization.AdminAccessInfo modelResponse) {
        return apiModelMapper.toApi(modelResponse);
    }

    public org.geoserver.acl.domain.rules.Rule toModel(Rule dto) {
        return apiModelMapper.toModel(dto);
    }

    public Rule toApi(org.geoserver.acl.domain.rules.Rule model) {
        return apiModelMapper.toApi(model);
    }

    public AccessSummaryRequest toModel(org.geoserver.acl.webapi.v1.model.AccessSummaryRequest request) {
        return apiModelMapper.toModel(request);
    }

    public AccessSummary toApi(org.geoserver.acl.authorization.AccessSummary modelResponse) {
        return apiModelMapper.toApi(modelResponse);
    }
}
