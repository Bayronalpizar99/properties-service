package com.roomiefy.properties_service.application.rest;


import com.roomiefy.properties_service.domain.model.Property;
import com.roomiefy.properties_service.domain.port.inbound.PropertyServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
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
    public Page<Property> getAllProperties(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) String amenities,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100); // Evita tamaños descontrolados
        Pageable pageable = PageRequest.of(safePage, safeSize);
        return propertyServicePort.getAllProperties(search, priceMax, bedrooms, amenities, pageable);
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
