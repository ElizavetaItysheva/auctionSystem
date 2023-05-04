package com.example.auctionsystem.repository;

import com.example.auctionsystem.model.Lot;
import com.example.auctionsystem.model.status.LotStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findAllByStatus( LotStatus lotStatus, PageRequest pageRequest);
@Query(nativeQuery = true, value = "SELECT start_price + ((SELECT count(bid_id) from bids where lot_lot_id = ?1) * start_price) from lots where lot_id = ?1")
    Integer getCurrentPriceById( Long id );
}
