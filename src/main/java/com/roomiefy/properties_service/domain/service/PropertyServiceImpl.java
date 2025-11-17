package com.roomiefy.properties_service.domain.service;

import com.roomiefy.properties_service.domain.model.Property;
import com.roomiefy.properties_service.domain.port.inbound.PropertyServicePort;
import com.roomiefy.properties_service.domain.port.outbound.PropertyRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; 

import java.util.List;
import java.util.Optional;

public class PropertyServiceImpl implements PropertyServicePort {

    private final PropertyRepositoryPort propertyRepositoryPort;

    public PropertyServiceImpl(PropertyRepositoryPort propertyRepositoryPort) {
        this.propertyRepositoryPort = propertyRepositoryPort;
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepositoryPort.save(property);
    }

    @Override
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepositoryPort.findById(id);
    }

    @Override
    public Page<Property> getAllProperties(String search, Double priceMax, Integer bedrooms, String amenities, Pageable pageable) {
        // 1. Creamos la especificación dinámica con los filtros
        Specification<Property> spec = PropertySpecification.withFilters(search, priceMax, bedrooms, amenities);
        
        // 2. Ejecutamos la consulta paginada usando el executor de especificaciones
        return propertyRepositoryPort.findAll(spec, pageable);
    }

    @Override
    public List<Property> getPropertiesByOwnerId(String ownerId) {
        // 3. Simplemente llamamos al nuevo método del repositorio
        return propertyRepositoryPort.findByOwnerId(ownerId);
    }

    public void deleteProperty(Long id) {
        // El JpaRepository nos da este método gratis
        propertyRepositoryPort.deleteById(id);
    }

    @Override
    public Optional<Property> updateProperty(Long id, Property propertyDetails) {
        // 1. Buscamos la propiedad existente por su ID
        return propertyRepositoryPort.findById(id).map(existingProperty -> {
            
            // 2. Actualizamos todos los campos con los nuevos detalles
            existingProperty.setName(propertyDetails.getName());
            existingProperty.setLocation(propertyDetails.getLocation());
            existingProperty.setPrice(propertyDetails.getPrice());
            existingProperty.setBedrooms(propertyDetails.getBedrooms());
            existingProperty.setBathrooms(propertyDetails.getBathrooms());
            existingProperty.setSquare_meters(propertyDetails.getSquare_meters());
            existingProperty.setDescription(propertyDetails.getDescription());
            existingProperty.setAmenities(propertyDetails.getAmenities());
            
            // Los campos del propietario (ownerId, etc.) no deberían cambiar
            // Tampoco cambiamos la foto o el rating en esta lógica simple

            // 3. Guardamos la propiedad actualizada en la base de datos
            return propertyRepositoryPort.save(existingProperty);
        });
    }
}
