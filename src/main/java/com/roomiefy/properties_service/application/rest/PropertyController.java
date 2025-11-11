package com.roomiefy.properties_service.application.rest;


import com.roomiefy.properties_service.domain.model.Property;
import com.roomiefy.properties_service.domain.port.inbound.PropertyServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyServicePort propertyServicePort;

    @Autowired
    public PropertyController(PropertyServicePort propertyServicePort) {
        this.propertyServicePort = propertyServicePort;
    }

    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyServicePort.createProperty(property);
    }

    @GetMapping
    public List<Property> getAllProperties(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) String amenities
    ) {
        return propertyServicePort.getAllProperties(search, priceMax, bedrooms, amenities);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        return propertyServicePort.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-properties/{ownerId}")
    public List<Property> getUserProperties(@PathVariable String ownerId) {
        return propertyServicePort.getPropertiesByOwnerId(ownerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyServicePort.deleteProperty(id);
        return ResponseEntity.noContent().build(); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property propertyDetails) {
        return propertyServicePort.updateProperty(id, propertyDetails)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }
}