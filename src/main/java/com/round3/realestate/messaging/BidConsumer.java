package com.round3.realestate.messaging;


import com.round3.realestate.entity.Auction;
import com.round3.realestate.repository.AuctionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BidConsumer {

    @Autowired
    private final AuctionRepository auctionRepository;

    @RabbitListener(queues = "bid.queue")
    @Transactional
    public void processBid(BidMessage bidMessage){
        Optional<Auction> optionalAuction = auctionRepository.findById(bidMessage.getAuctionId());
        if(optionalAuction.isEmpty()){
            log.warn("Auction not found: " + bidMessage.getAuctionId());
            return;
        }
        Auction auction = optionalAuction.get();
        if (auction.isClosed()) {
            log.warn("Auction is already closed: " + auction.getId());
            return;
        }
        if (bidMessage.getBidAmount().compareTo(auction.getCurrentHighestBid().add(auction.getMinBidIncrement())) >= 0) {
            auction.setCurrentHighestBid(bidMessage.getBidAmount());
            auction.setWinningUserId(bidMessage.getUserId());
            auctionRepository.save(auction);
            log.info("Bid accepted: " + bidMessage.getBidAmount() + " for auction " + auction.getId());
        } else {
            log.warn("Bid too low: " + bidMessage.getBidAmount());
        }
    }

}
