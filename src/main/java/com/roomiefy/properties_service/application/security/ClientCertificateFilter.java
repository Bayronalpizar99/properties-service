package com.roomiefy.properties_service.application.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ClientCertificateFilter extends OncePerRequestFilter {

    public static final String CLIENT_CERT_HEADER = "X-ARR-ClientCert";

    private static final Logger log = LoggerFactory.getLogger(ClientCertificateFilter.class);

    private final ClientCertificateProperties properties;

    public ClientCertificateFilter(ClientCertificateProperties properties) {
        this.properties = properties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !properties.isEnabled() || HttpMethod.OPTIONS.matches(request.getMethod());
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        if (CollectionUtils.isEmpty(properties.getAllowedThumbprints())) {
            log.warn("Client certificate validation enabled but no thumbprints configured");
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Certificate validation misconfigured");
            return;
        }

        String certificateHeader = request.getHeader(CLIENT_CERT_HEADER);
        if (certificateHeader == null || certificateHeader.isBlank()) {
            log.warn("Missing {} header in request {}", CLIENT_CERT_HEADER, request.getRequestURI());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Client certificate is required");
            return;
        }

        try {
            X509Certificate certificate = parseCertificate(certificateHeader);
            String thumbprint = getThumbprint(certificate);

            if (!properties.getAllowedThumbprints().contains(thumbprint)) {
                log.warn("Rejected request with unexpected certificate thumbprint {}", thumbprint);
                response.sendError(HttpStatus.FORBIDDEN.value(), "Client certificate is not allowed");
                return;
            }
        } catch (CertificateException | NoSuchAlgorithmException e) {
            log.error("Failed to parse client certificate", e);
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid client certificate");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private X509Certificate parseCertificate(String headerValue) throws CertificateException {
        byte[] decoded = Base64.getDecoder().decode(headerValue);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(decoded));
    }

    private String getThumbprint(X509Certificate certificate)
        throws CertificateEncodingException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashed = digest.digest(certificate.getEncoded());
        StringBuilder builder = new StringBuilder();
        for (byte b : hashed) {
            builder.append(String.format(Locale.ROOT, "%02X", b));
        }
        return builder.toString();
    }
}
