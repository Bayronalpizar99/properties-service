package com.roomiefy.properties_service.domain.port.inbound;

import com.roomiefy.properties_service.domain.model.Property;
import java.util.List;
import java.util.Optional;

public interface PropertyServicePort {
    
    Property createProperty(Property property);
    Optional<Property> getPropertyById(Long id);
    List<Property> getAllProperties(String search, Double priceMax, Integer bedrooms, String amenities);
    List<Property> getPropertiesByOwnerId(String ownerId);
    void deleteProperty(Long id);
    Optional<Property> updateProperty(Long id, Property propertyDetails);
}