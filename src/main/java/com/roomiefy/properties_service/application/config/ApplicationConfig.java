package com.roomiefy.properties_service.application.config;

import com.roomiefy.properties_service.domain.port.inbound.PropertyServicePort;
import com.roomiefy.properties_service.domain.port.outbound.PropertyRepositoryPort;
import com.roomiefy.properties_service.domain.service.PropertyServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public PropertyServicePort propertyServicePort(PropertyRepositoryPort propertyRepositoryPort) {
        return new PropertyServiceImpl(propertyRepositoryPort);
    }
}