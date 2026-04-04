/* (c) 2023  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.webapi.v1.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.geoserver.acl.webapi.v1.mapper.AclApiModelMapper;
import org.geoserver.acl.webapi.v1.model.AdminRuleFilter;
import org.geoserver.acl.webapi.v1.model.InsertPosition;
import org.geoserver.acl.webapi.v1.model.RuleFilter;
import org.geoserver.acl.webapi.v1.server.RequestBodyBufferingServletFilter.RequestBodyBufferingServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectReader;

@RequiredArgsConstructor
public abstract class ApiImplSupport<D, T> {

    protected final @NonNull NativeWebRequest nativeRequest;
    protected final @NonNull AclApiModelMapper apiModelMapper = new AclApiModelMapper();

    public abstract T toModel(D dto);

    public abstract D toApi(T model);

    public InsertPosition toApi(org.geoserver.acl.domain.rules.InsertPosition position) {
        return apiModelMapper.toApi(position);
    }

    public org.geoserver.acl.domain.rules.InsertPosition toRuleModel(InsertPosition position) {
        return apiModelMapper.toRuleModel(position);
    }

    public org.geoserver.acl.domain.adminrules.InsertPosition toAdminRulesModel(InsertPosition position) {
        return apiModelMapper.toAdminRuleModel(position);
    }

    public org.geoserver.acl.domain.adminrules.AdminRuleFilter map(AdminRuleFilter adminRuleFilter) {
        return apiModelMapper.toModel(adminRuleFilter);
    }

    public org.geoserver.acl.domain.rules.RuleFilter map(RuleFilter filter) {
        return apiModelMapper.toModel(filter);
    }

    public T mergePatch(final T orig) {
        D merged;
        try {
            RequestBodyBufferingServletRequest bufferedRequest =
                    nativeRequest.getNativeRequest(RequestBodyBufferingServletRequest.class);
            Objects.requireNonNull(
                    bufferedRequest, "Servlet Filter not set up, expected RequestBodyBufferingServletRequest");
            BufferedReader reader = bufferedRequest.getReader();

            D current = toApi(orig);
            ObjectReader readerForUpdating = new ObjectMapper().readerForUpdating(current);
            merged = readerForUpdating.readValue(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return toModel(merged);
    }

    public <R> ResponseEntity<R> error(HttpStatus code, String reason) {
        return ResponseEntity.status(code).header("X-Reason", reason).build();
    }

    public void setPreferredGeometryEncoding() {
        String acceptContentType = nativeRequest.getHeader("Accept");
        boolean useWkb = true;
        if (StringUtils.hasText(acceptContentType)) {
            try {
                String contentType = acceptContentType.split(",")[0];
                MediaType mediaType = MediaType.parseMediaType(contentType);
                useWkb = !MediaType.APPLICATION_JSON.isCompatibleWith(mediaType);
            } catch (Exception e) {
                useWkb = false;
            }
        } else {
            useWkb = false;
        }
        apiModelMapper.setUseWkb(useWkb);
    }
}
