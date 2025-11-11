package com.roomiefy.properties_service.domain.service;

import com.roomiefy.properties_service.domain.model.Property;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    // Especificación principal que combina todas las demás
    public static Specification<Property> withFilters(String search, Double priceMax, Integer bedrooms, String amenities) {
        
        // (root, query, builder) -> builder.conjunction() es un "WHERE 1=1"
        return (root, query, builder) -> {
            
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtro de Búsqueda (search)
            if (search != null && !search.isEmpty()) {
                String searchLike = "%" + search.toLowerCase() + "%";
                // Busca en el nombre O en la ubicación O en la descripción
                predicates.add(builder.or(
                    builder.like(builder.lower(root.get("name")), searchLike),
                    builder.like(builder.lower(root.get("location")), searchLike),
                    builder.like(builder.lower(root.get("description")), searchLike)
                ));
            }

            // 2. Filtro de Precio Máximo (priceMax)
            if (priceMax != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            // 3. Filtro de Habitaciones (bedrooms)
            if (bedrooms != null) {
                predicates.add(builder.equal(root.get("bedrooms"), bedrooms));
            }

            // 4. Filtro de Amenities (amenities)
            if (amenities != null && !amenities.isEmpty()) {
                // El front-end envía "Wi-Fi,Piscina"
                String[] amenityList = amenities.split(",");
                for (String amenity : amenityList) {
                    // Busca que la amenidad esté en el string de la DB (ej. "Wi-Fi,TV,Cocina")
                    predicates.add(builder.like(root.get("amenities"), "%" + amenity.trim() + "%"));
                }
            }

            // Combina todos los filtros con un "AND"
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}