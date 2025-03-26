package com.round3.realestate.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class DashboardResponse {

    private String employmentStatus;
    private String contractType;
    private BigDecimal netMonthlySalary;
    private List<MortgageResponse> mortgages;
}
