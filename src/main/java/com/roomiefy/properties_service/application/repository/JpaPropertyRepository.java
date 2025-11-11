package com.roomiefy.properties_service.application.repository;

import com.roomiefy.properties_service.domain.port.outbound.PropertyRepositoryPort;
import org.springframework.stereotype.Repository;

/**
 * Este es el Adaptador de Salida.
 * Spring Data "verá" que extiende PropertyRepositoryPort,
 * que a su vez extiende JpaRepository y JpaSpecificationExecutor,
 * y mágicamente creará la implementación por nosotros.
 */
@Repository("jpaPropertyRepository") // Le damos un nombre explícito
public interface JpaPropertyRepository extends PropertyRepositoryPort {
    
}