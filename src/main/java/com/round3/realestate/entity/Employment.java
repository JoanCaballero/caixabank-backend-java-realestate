package com.round3.realestate.entity;

import com.round3.realestate.entity.enumerations.ContractType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employment", schema = "realestate")
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    private BigDecimal grossSalary;
    private BigDecimal netMonthlySalary;

    public Employment() {
    }

    public Employment(Long id, User user, ContractType contractType, BigDecimal grossSalary, BigDecimal netMonthlySalary) {
        this.id = id;
        this.user = user;
        this.contractType = contractType;
        this.grossSalary = grossSalary;
        this.netMonthlySalary = netMonthlySalary;
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

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }

    public BigDecimal getNetMonthlySalary() {
        return netMonthlySalary;
    }

    public void setNetMonthlySalary(BigDecimal netMonthlySalary) {
        this.netMonthlySalary = netMonthlySalary;
    }
}
