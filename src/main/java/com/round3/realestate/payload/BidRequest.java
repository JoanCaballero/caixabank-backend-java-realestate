package com.round3.realestate.payload;



import java.math.BigDecimal;


public class BidRequest {
    private BigDecimal bidAmount;

    public BidRequest(){}
    public BidRequest(BigDecimal bidAmount){
        this.bidAmount = bidAmount;
    }

    public BigDecimal getBidAmount(){
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount){
        this.bidAmount = bidAmount;
    }
}
