package com.roomiefy.properties_service.application.security;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties(prefix = "security.client-cert")
public class ClientCertificateProperties {

    /**
     * Flag to enable/disable certificate validation. Disabled by default for local development.
     */
    private boolean enabled;

    /**
     * Allowed certificate thumbprints (SHA-1) provided as comma separated values.
     */
    private Set<String> allowedThumbprints = new HashSet<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getAllowedThumbprints() {
        return Collections.unmodifiableSet(allowedThumbprints);
    }

    public void setAllowedThumbprints(List<String> thumbprints) {
        if (thumbprints == null) {
            this.allowedThumbprints = Collections.emptySet();
            return;
        }

        this.allowedThumbprints = thumbprints.stream()
            .filter(StringUtils::hasText)
            .map(value -> value.replace(" ", "").toUpperCase(Locale.ROOT))
            .collect(Collectors.toSet());
    }
}
