package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageResponse {

    private Long id;
    private BigDecimal totalCost;
    private BigDecimal monthlyPayment;
    private Integer termYears;
    private Boolean approved;
}
