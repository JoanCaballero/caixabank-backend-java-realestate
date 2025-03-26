package com.round3.realestate.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mortgages", schema = "realestate")
public class Mortgage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private BigDecimal totalCost;
    private BigDecimal monthlyPayment;
    private Boolean approved;
    private Integer termYears;

    public Mortgage() {
    }

    public Mortgage(Long id, User user, Property property, BigDecimal totalCost, BigDecimal monthlyPayment, Boolean approved, Integer termYears) {
        this.id = id;
        this.user = user;
        this.property = property;
        this.totalCost = totalCost;
        this.monthlyPayment = monthlyPayment;
        this.approved = approved;
        this.termYears = termYears;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Integer getTermYears() {
        return termYears;
    }

    public void setTermYears(Integer termYears) {
        this.termYears = termYears;
    }
}
