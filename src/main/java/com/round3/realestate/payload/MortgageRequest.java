package com.round3.realestate.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class MortgageRequest {
    private Long propertyId;
    private Integer termYears;
}
