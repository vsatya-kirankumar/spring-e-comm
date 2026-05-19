package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must have at least 5 characters.")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must have at least 5 characters.")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name must have at least 5 characters.")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must have at least 2 characters.")
    private String state;

    @NotBlank
    @Size(min = 6, message = "Pincode must have at least 6 characters.")
    private Integer pincode;

    @NotBlank
    @Size(min = 2, message = "Country name must have at least 2 characters.")
    private String country;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<UserInfo> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, Integer pincode, String country) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
    }
}