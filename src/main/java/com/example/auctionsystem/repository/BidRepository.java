package com.example.auctionsystem.repository;

import com.example.auctionsystem.model.Bid;
import com.example.auctionsystem.projections.FrequentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query(nativeQuery = true, value =
            "SELECT bidder_name as bidderName, max(bid_date) as bidDate FROM bids WHERE lot_lot_id = ?1 GROUP BY bidder_name ORDER BY count(*) desc limit 1")
    FrequentView findFrequentOne( Long lotId);
}
