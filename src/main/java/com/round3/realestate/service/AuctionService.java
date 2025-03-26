package com.round3.realestate.service;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.enumerations.AuctionStatus;
import com.round3.realestate.entity.Property;
import com.round3.realestate.messaging.BidMessage;
import com.round3.realestate.payload.AuctionRequest;
import com.round3.realestate.payload.BidRequest;
import com.round3.realestate.repository.AuctionRepository;
import com.round3.realestate.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    @Autowired
    private final AuctionRepository auctionRepository;
    @Autowired
    private final PropertyRepository propertyRepository;
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public Auction createAuction(AuctionRequest auctionRequest){
        Property property = propertyRepository.findById(auctionRequest.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));
        Auction auction = new Auction();
        auction.setProperty(property);
        auction.setStartTime(auctionRequest.getStartTime());
        auction.setEndTime(auctionRequest.getEndTime());
        auction.setStartingPrice(auctionRequest.getStartingPrice());
        auction.setMinBidIncrement(auctionRequest.getMinBidIncrement());
        auction.setCurrentHighestBid(auctionRequest.getStartingPrice());
        auction.setStatus(AuctionStatus.OPEN);

        return auctionRepository.save(auction);
    }

    @Transactional
    public String placeBid(Long auctionId, Long userId, BidRequest bidRequest){
        var auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found with ID: " + auctionId));

        if (auction.isClosed()) {
            return "Auction is already closed.";
        }

        var minValidBid = auction.getCurrentHighestBid().add(auction.getMinBidIncrement());
        if (bidRequest.getBidAmount().compareTo(minValidBid) < 0) {
            return "Bid is too low. Minimum required bid: " + minValidBid;
        }

        var bidMessage = new BidMessage(auctionId, userId, bidRequest.getBidAmount(), LocalDateTime.now());
        rabbitTemplate.convertAndSend("bid.exchange", "bid.routingkey", bidMessage);

        return "Bid placed successfully.";
    }

    public Auction findById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public Auction save(Auction auction){
        return auctionRepository.save(auction);
    }
}
