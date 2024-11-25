package com.romashkaco.myproducts_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is mandatory")
    @NotNull
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 4096, message = "Description must not exceed 4096 characters")
    private String description;

    @Min(value = 0, message = "Price must be at least 0")
    private double price = 0;

    private boolean available = false;
}
