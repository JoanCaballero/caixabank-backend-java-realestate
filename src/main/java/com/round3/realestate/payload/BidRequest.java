package com.round3.realestate.payload;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BidRequest {
    private BigDecimal bidAmount;
}
