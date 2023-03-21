package com.example.auctionsystem.repository;

import com.example.auctionsystem.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findAllByStatusContainingIgnoreCase( String status);

}
