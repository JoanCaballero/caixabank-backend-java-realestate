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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuctionService {

    private AuctionRepository auctionRepository;
    private PropertyRepository propertyRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository, PropertyRepository propertyRepository, RabbitTemplate rabbitTemplate){
        this.auctionRepository = auctionRepository;
        this.propertyRepository = propertyRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

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
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        if(optionalAuction.isEmpty()){
            return "Auction not found.";
        }
        Auction auction = optionalAuction.get();
        if(auction.isClosed()){
            return "Auction is already closed.";
        }
        BigDecimal currentHighestBid = auction.getCurrentHighestBid();
        BigDecimal minIncrement = auction.getMinBidIncrement();

        if (bidRequest.getBidAmount().compareTo(currentHighestBid.add(minIncrement)) < 0) {
            return "Bid is too low. Minimum bid: " + currentHighestBid.add(minIncrement);
        }
        BidMessage bidMessage = new BidMessage(auctionId, userId, bidRequest.getBidAmount(), LocalDateTime.now());
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
