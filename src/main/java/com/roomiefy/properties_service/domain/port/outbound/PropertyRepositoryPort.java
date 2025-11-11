package com.roomiefy.properties_service.domain.port.outbound;

import com.roomiefy.properties_service.domain.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean; 
import java.util.List;
/**
 * Este es el Puerto de Salida.
 * Define el contrato COMPLETO que el dominio necesita para la persistencia.
 */
@NoRepositoryBean 
public interface PropertyRepositoryPort extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    List<Property> findByOwnerId(String ownerId);
    
}