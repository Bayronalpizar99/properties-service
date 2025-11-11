package com.roomiefy.properties_service.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerId;
    private String name; 
    private String location; 
    private Double price; 
    @Column(length = 1024) 
    private String description;
    private int bedrooms;
    private int bathrooms;
    private int square_meters;
    @Column(length = 1024)
    private String property_photo;
    @Column(length = 1024)
    private String amenities; 
    private String owner_name;
    @Column(length = 1024)
    private String owner_profile_pic;
    private int rating;
}