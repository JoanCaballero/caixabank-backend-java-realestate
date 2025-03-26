package com.round3.realestate.repository;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.enumerations.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Auction findByIdAndStatus(Long id, AuctionStatus status);

    Optional<Auction> findById(Long id);
}
