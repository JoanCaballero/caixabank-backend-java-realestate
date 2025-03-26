package com.round3.realestate.payload;

import com.round3.realestate.entity.enumerations.ContractType;
import java.math.BigDecimal;


public class EmploymentRequest {
    private ContractType contractType;
    private BigDecimal grossSalary;

    public EmploymentRequest() {
    }

    public EmploymentRequest(ContractType contractType, BigDecimal grossSalary) {
        this.contractType = contractType;
        this.grossSalary = grossSalary;
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
}
