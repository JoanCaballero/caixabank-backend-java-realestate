package com.round3.realestate.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidMessage {

    private Long auctionId;
    private Long userId;
    private BigDecimal bidAmount;
    private LocalDateTime timestamp;
}
