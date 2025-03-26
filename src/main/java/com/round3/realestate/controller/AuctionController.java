package com.round3.realestate.controller;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.enumerations.Availability;
import com.round3.realestate.entity.Bid;
import com.round3.realestate.entity.enumerations.AuctionStatus;
import com.round3.realestate.entity.Property;
import com.round3.realestate.messaging.BidMessage;
import com.round3.realestate.payload.AuctionRequest;
import com.round3.realestate.service.AuctionService;
import com.round3.realestate.service.PropertyService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {
    private AuctionService auctionService;
    private PropertyService propertyService;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public AuctionController(AuctionService auctionService, PropertyService propertyService, RabbitTemplate rabbitTemplate) {
        this.auctionService = auctionService;
        this.propertyService = propertyService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/create")
    public Auction createAuction(@RequestBody AuctionRequest auctionRequest){
        return auctionService.createAuction(auctionRequest);
    }

    @PostMapping("/{auctionId}/bid")
    public ResponseEntity<String> placeBid(@PathVariable Long auctionId, @RequestBody Bid bid, @AuthenticationPrincipal UserDetails userDetails){
        Auction auction = auctionService.findById(auctionId);
        if (auction == null || auction.isClosed()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Auction is not open.");
        }
        BigDecimal currentHighestBid = auction.getCurrentHighestBid();
        BigDecimal minIncrement = auction.getMinBidIncrement();

        if(bid.getBidAmount().compareTo(currentHighestBid.add(minIncrement))< 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bid is too low. Minimum bid: " + currentHighestBid.add(minIncrement));
        }
        BidMessage bidMessage = new BidMessage(auctionId, Long.parseLong(userDetails.getUsername()), bid.getBidAmount(), LocalDateTime.now());

        rabbitTemplate.convertAndSend("bid.exchange", "bid.routingkey", bidMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bid placed successfully.");
    }

    @PatchMapping("/{auctionId}/close")
    public ResponseEntity<Map<String, Object>> closeAuction(@PathVariable Long auctionId) {
        Auction auction = auctionService.findById(auctionId);
        if (auction == null || auction.getStatus() == AuctionStatus.CLOSED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Auction is not open or already closed."));
        }

        List<Bid> bids = auction.getBids();

        if (bids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No bids have been placed."));
        }

        Bid winningBid = bids.stream()
                .max(Comparator.comparing(Bid::getBidAmount))
                .orElseThrow(() -> new RuntimeException("Error determining the highest bid"));

        auction.setStatus(AuctionStatus.CLOSED);
        auctionService.save(auction);

        Property property = auction.getProperty();
        property.setAvailability(Availability.UNAVAILABLE);
        propertyService.save(property);

        Map<String, Object> response = new HashMap<>();
        response.put("winnerId", winningBid.getUser().getId());
        response.put("winningBidAmount", winningBid.getBidAmount());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
