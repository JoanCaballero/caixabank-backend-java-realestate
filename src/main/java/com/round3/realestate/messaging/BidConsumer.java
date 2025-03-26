package com.round3.realestate.messaging;


import com.round3.realestate.entity.Auction;
import com.round3.realestate.repository.AuctionRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BidConsumer {

    private final AuctionRepository auctionRepository;

    @Autowired
    public BidConsumer (AuctionRepository auctionRepository){
        this.auctionRepository = auctionRepository;
    }

    @RabbitListener(queues = "bid.queue")
    @Transactional
    public void processBid(BidMessage bidMessage){
        Optional<Auction> optionalAuction = auctionRepository.findById(bidMessage.getAuctionId());
        if(optionalAuction.isEmpty()){
            System.out.println("Auction not found: " + bidMessage.getAuctionId());
            return;
        }
        Auction auction = optionalAuction.get();
        if (auction.isClosed()) {
            System.out.println("Auction is already closed: " + auction.getId());
            return;
        }
        if (bidMessage.getBidAmount().compareTo(auction.getCurrentHighestBid().add(auction.getMinBidIncrement())) >= 0) {
            auction.setCurrentHighestBid(bidMessage.getBidAmount());
            auction.setWinningUserId(bidMessage.getUserId());
            auctionRepository.save(auction);
            System.out.println("Bid accepted: " + bidMessage.getBidAmount() + " for auction " + auction.getId());
        } else {
            System.out.println("Bid too low: " + bidMessage.getBidAmount());
        }
    }

}
