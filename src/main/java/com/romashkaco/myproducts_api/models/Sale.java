package com.romashkaco.myproducts_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Document name is mandatory")
    @Size(max = 255, message = "Document name must not exceed 255 characters")
    private String documentName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull
    @Column(nullable = false)
    private double totalPrice;

}
