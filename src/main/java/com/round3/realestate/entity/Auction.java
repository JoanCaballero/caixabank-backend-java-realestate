package com.round3.realestate.entity;

import com.round3.realestate.entity.enumerations.AuctionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions", schema = "realestate")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "property_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Property property;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal startingPrice;
    private BigDecimal minBidIncrement;
    private BigDecimal currentHighestBid;
    @Enumerated(EnumType.STRING)
    private AuctionStatus status;
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bid> bids = new ArrayList<>();
    private Long winningUserId;

    public Auction() {
    }

    public Auction(Long id, Property property, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingPrice,
                   BigDecimal minBidIncrement, BigDecimal currentHighestBid, AuctionStatus status,
                   List<Bid> bids, Long winningUserId) {
        this.id = id;
        this.property = property;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingPrice = startingPrice;
        this.minBidIncrement = minBidIncrement;
        this.currentHighestBid = currentHighestBid;
        this.status = status;
        this.bids = bids;
        this.winningUserId = winningUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public BigDecimal getMinBidIncrement() {
        return minBidIncrement;
    }

    public void setMinBidIncrement(BigDecimal minBidIncrement) {
        this.minBidIncrement = minBidIncrement;
    }

    public BigDecimal getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(BigDecimal currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Long getWinningUserId() {
        return winningUserId;
    }

    public void setWinningUserId(Long winningUserId) {
        this.winningUserId = winningUserId;
    }

    public boolean isClosed(){
        return this.status == AuctionStatus.CLOSED;
    }
}
