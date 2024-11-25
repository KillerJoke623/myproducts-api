package com.romashkaco.myproducts_api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private int id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 4096, message = "Description must not exceed 4096 characters")
    private String description;

    @Min(value = 0, message = "Price must be at least 0")
    private double price = 0;

    private boolean available = false;
}
