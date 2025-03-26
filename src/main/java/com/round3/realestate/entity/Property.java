package com.round3.realestate.entity;

import com.round3.realestate.entity.enumerations.Availability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "properties", schema = "realestate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String title;
    private String location;
    private BigDecimal price;
    private int size;
    private int rooms;
    @Enumerated(EnumType.STRING)
    private Availability availability;
}
