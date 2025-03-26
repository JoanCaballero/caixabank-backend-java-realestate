package com.round3.realestate.messaging;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidMessage {

    private Long auctionId;
    private Long userId;
    private BigDecimal bidAmount;
    private LocalDateTime timestamp;

    public BidMessage() {
    }

    public BidMessage(Long auctionId, Long userId, BigDecimal bidAmount, LocalDateTime timestamp) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.timestamp = timestamp;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
