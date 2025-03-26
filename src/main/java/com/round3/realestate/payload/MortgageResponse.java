package com.round3.realestate.payload;

import java.math.BigDecimal;

public class MortgageResponse {

    private Long id;
    private BigDecimal totalCost;
    private BigDecimal monthlyPayment;
    private Integer termYears;
    private Boolean approved;

    public MortgageResponse(){}
    public MortgageResponse(Long id, BigDecimal totalCost, BigDecimal monthlyPayment, Integer termYears, Boolean approved) {
        this.id = id;
        this.totalCost = totalCost;
        this.monthlyPayment = monthlyPayment;
        this.termYears = termYears;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getTermYears() {
        return termYears;
    }

    public void setTermYears(Integer termYears) {
        this.termYears = termYears;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
