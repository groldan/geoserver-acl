/* (c) 2026  Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.acl.autoconfigure.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Caches the result of {@link PasswordEncoder#matches} to avoid expensive BCrypt hashing on
 * every HTTP Basic Auth request. Uses a Caffeine cache bounded by size and TTL to prevent
 * unbounded memory growth from brute-force attempts. The cache key is the raw+encoded password
 * pair (hashed via {@link String#hashCode} to avoid storing plaintext).
 *
 * @since 3.0
 */
@RequiredArgsConstructor
class CachingPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate;

    private final Cache<String, Boolean> cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    @Override
    public String encode(CharSequence rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String key = rawPassword.toString().hashCode() + ":" + encodedPassword;
        return cache.get(key, k -> delegate.matches(rawPassword, encodedPassword));
    }
}
