package com.hromenko.computerperipherals.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Peripheral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String brand;

    @Column
    private String model;

    @Column(name="additional_info")
    private String additionalInfo;

    @Column
    private String color;

    @Column
    private float price;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;
}
