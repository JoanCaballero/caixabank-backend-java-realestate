package com.round3.realestate.payload;

public class MortgageRequest {
    private Long propertyId;
    private Integer termYears;

    public MortgageRequest() {
    }

    public MortgageRequest(Long propertyId, Integer termYears) {
        this.propertyId = propertyId;
        this.termYears = termYears;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getTermYears() {
        return termYears;
    }

    public void setTermYears(Integer termYears) {
        this.termYears = termYears;
    }
}
