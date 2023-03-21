package com.example.auctionsystem.service;

import com.example.auctionsystem.dto.BidDTO;
import com.example.auctionsystem.dto.CreateLotDTO;
import com.example.auctionsystem.dto.FullLotDTO;
import com.example.auctionsystem.dto.LotDTO;
import com.example.auctionsystem.model.Bid;
import com.example.auctionsystem.model.Lot;
import com.example.auctionsystem.model.status.LotStatus;
import com.example.auctionsystem.projections.FrequentView;
import com.example.auctionsystem.repository.BidRepository;
import com.example.auctionsystem.repository.LotRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    public LotService( LotRepository lotRepository, BidRepository bidRepository ) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
    }


    public Lot createLot(CreateLotDTO createLotDTO){
        Lot createdLot = createLotDTO.toLot();
        createdLot.setStatus(LotStatus.CREATED.toString());
        lotRepository.save(createdLot);
        return createdLot;
    }
    public void startBargain(Long lotId){
        // находим лот, переводим в статус начато и сохраняем
        Lot foundedLot = lotRepository.findById(lotId).get();
        foundedLot.setStatus(LotStatus.STARTED.toString());
        lotRepository.save(foundedLot);
    }
    public void createBid(Long lotId, String bidName){
        // находим лот
        Lot foundedLot = lotRepository.findById(lotId).get();
        // создаем бид
        Bid bid = new Bid();
        bid.setLot(foundedLot);
        bid.setBidderName(bidName);
        bid.setBidDate(LocalDate.now().toString());
        bidRepository.save(bid);

    }
    public void stopBargain(Long id){
        // находим лот, переводим в статус остановлено и сохраняем
        Lot foundedLot = lotRepository.findById(id).get();
        foundedLot.setStatus(LotStatus.STOPPED.toString());
        lotRepository.save(foundedLot);
    }
    public FullLotDTO getFullLot(Long id){
        return FullLotDTO.fromLot(lotRepository.findById(id).get());
    }
    public BidDTO getFirstBidPerson(Long id){
        Lot founded = lotRepository.findById(id).get();
        return BidDTO.fromBid(founded.getBids().get(0));
    }
    public FrequentView getFrequentBid(Long id){
        Lot founded = lotRepository.findById(id).get();
        return bidRepository.findFrequentOne(id);
    }
    public List<LotDTO> getAllLots(String status, Integer pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber -1, 10);
        List<Lot> founded = lotRepository.findAllByStatusContainingIgnoreCase(status);
        List<LotDTO> needed = new ArrayList<>();
        for(Lot lot: founded){
            LotDTO dto = LotDTO.fromLot(lot);
            needed.add(dto);
        }
        return needed;
    }
//    public String export() throws IOException {
//
//
//
//    }
    public Lot findLotById(Long id){
        return lotRepository.findById(id).get();
    }

}
