package com.round3.realestate.entity;

import com.round3.realestate.entity.enumerations.Availability;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "properties", schema = "realestate")
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

    public Property() {
    }

    public Property(Long id, String name, String title, String location, BigDecimal price, int size, int rooms, Availability availability) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.location = location;
        this.price = price;
        this.size = size;
        this.rooms = rooms;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
