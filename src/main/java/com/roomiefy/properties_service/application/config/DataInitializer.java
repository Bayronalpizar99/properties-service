package com.roomiefy.properties_service.application.config;

import com.roomiefy.properties_service.domain.model.Property;
import com.roomiefy.properties_service.domain.port.outbound.PropertyRepositoryPort;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile; 
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@Profile("dev") 
public class DataInitializer {

    @Transactional
    @Bean
    CommandLineRunner loadDatabase(PropertyRepositoryPort propertyRepository) {
        return args -> {
            if (propertyRepository.count() == 0) {
                System.out.println("Base de datos vacía. Cargando propiedades de muestra...");
                
                List<Property> properties = List.of(
                    Property.builder()
                        .ownerId("user-id-1") 
                        .name("Acogedor Apartamento en el Centro").location("San José, Costa Rica").price(75.0)
                        .description("Un refugio moderno y luminoso en el corazón de la ciudad, perfecto para explorar la vida urbana a pie. Ideal para parejas o viajeros de negocios.")
                        .bedrooms(1).bathrooms(1).square_meters(60).property_photo("https://images.unsplash.com/photo-1512917774080-9991f1c4c750")
                        .amenities("Wi-Fi de alta velocidad,Aire acondicionado,Cocina equipada,TV con Netflix")
                        .owner_name("Ana Rodriguez").owner_profile_pic("https://images.unsplash.com/photo-1438761681033-6461ffad8d80")
                        .rating(5).build(),
                    Property.builder()
                        .ownerId("user-id-2") 
                        .name("Villa de Lujo con Vista al Mar").location("Manuel Antonio, Puntarenas").price(250.0)
                        .description("Experimenta el paraíso en esta villa de lujo con vistas infinitas al Océano Pacífico. Un escape perfecto con todas las comodidades modernas.")
                        .bedrooms(3).bathrooms(3).square_meters(280).property_photo("https://images.unsplash.com/photo-1580587771525-78b9dba3b914")
                        .amenities("Piscina privada,Acceso a la playa,Servicio de limpieza,Parqueo privado")
                        .owner_name("Carlos Varela").owner_profile_pic("https://images.unsplash.com/photo-1500648767791-00dcc994a43e")
                        .rating(4).build(),
                    Property.builder()
                        .ownerId("user-id-3") 
                        .name("Cabaña Rústica en la Montaña").location("Monteverde, Puntarenas").price(110.0)
                        .description("Desconéctate del mundo en esta encantadora cabaña de madera, rodeada por la serenidad del bosque nuboso de Monteverde.")
                        .bedrooms(2).bathrooms(1).square_meters(95).property_photo("https://images.unsplash.com/photo-1605276374104-dee2a0ed3cd6?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .amenities("Chimenea,Wi-Fi,Senderos cercanos,Pet-Friendly")
                        .owner_name("Sofía Morales").owner_profile_pic("https://images.unsplash.com/photo-1580489944761-15a19d654956")
                        .rating(5).build()
                );
                
                propertyRepository.saveAll(properties);
                System.out.println("Base de datos cargada con " + properties.size() + " propiedades de muestra.");
            
            } else {
                System.out.println("La base de datos ya tiene datos. No se añaden datos de muestra.");
            }
        };
    }
}