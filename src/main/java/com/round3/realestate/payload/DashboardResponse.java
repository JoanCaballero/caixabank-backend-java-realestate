package com.round3.realestate.payload;

import java.math.BigDecimal;
import java.util.List;


public class DashboardResponse {

    private String employmentStatus;
    private String contractType;
    private BigDecimal netMonthlySalary;
    private List<MortgageResponse> mortgages;

    public DashboardResponse() {
    }

    public DashboardResponse(String employmentStatus, String contractType, BigDecimal netMonthlySalary, List<MortgageResponse> mortgages) {
        this.employmentStatus = employmentStatus;
        this.contractType = contractType;
        this.netMonthlySalary = netMonthlySalary;
        this.mortgages = mortgages;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public BigDecimal getNetMonthlySalary() {
        return netMonthlySalary;
    }

    public void setNetMonthlySalary(BigDecimal netMonthlySalary) {
        this.netMonthlySalary = netMonthlySalary;
    }

    public List<MortgageResponse> getMortgages() {
        return mortgages;
    }

    public void setMortgages(List<MortgageResponse> mortgages) {
        this.mortgages = mortgages;
    }
}
