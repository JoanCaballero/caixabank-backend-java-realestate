package com.round3.realestate.payload;

import com.round3.realestate.entity.enumerations.ContractType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class EmploymentRequest {
    private ContractType contractType;
    private BigDecimal grossSalary;
}
