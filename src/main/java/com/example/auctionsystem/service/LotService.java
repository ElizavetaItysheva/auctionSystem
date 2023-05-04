package com.example.auctionsystem.service;

import com.example.auctionsystem.dto.BidDTO;
import com.example.auctionsystem.dto.CreateLotDTO;
import com.example.auctionsystem.dto.FullLotDTO;
import com.example.auctionsystem.dto.LotDTO;
import com.example.auctionsystem.exception.LotNotFoundException;
import com.example.auctionsystem.model.Bid;
import com.example.auctionsystem.model.Lot;
import com.example.auctionsystem.model.status.LotStatus;
import com.example.auctionsystem.projections.FrequentView;
import com.example.auctionsystem.repository.BidRepository;
import com.example.auctionsystem.repository.LotRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    public LotService( LotRepository lotRepository, BidRepository bidRepository ) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
    }


    public LotDTO createLot( CreateLotDTO createLotDTO ) {
        if (createLotDTO == null) {
            throw new NullPointerException();
        }
        Lot createdLot = createLotDTO.toLot();
        if (createdLot == null) {
            throw new LotNotFoundException();
        }
        createdLot.setStatus(LotStatus.CREATED);
        lotRepository.save(createdLot);
        return LotDTO.fromLot(createdLot);
    }
    public void startBargain( Long lotId ) {
        Lot foundedLot = lotRepository.findById(lotId).orElseThrow(LotNotFoundException::new);
        foundedLot.setStatus(LotStatus.STARTED);
        lotRepository.save(foundedLot);
    }

    public void createBid( Long lotId, String bidName ) {
        Lot foundedLot = lotRepository.findById(lotId).orElseThrow(LotNotFoundException::new);

        Bid bid = new Bid();
        bid.setLot(foundedLot);
        bid.setBidderName(bidName);
        bid.setBidDate(LocalDate.now().toString());
        bidRepository.save(bid);

    }

    public void stopBargain( Long id ) {
        Lot foundedLot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        foundedLot.setStatus(LotStatus.STOPPED);
        lotRepository.save(foundedLot);
    }

    public FullLotDTO getFullLot( Long id ) {
        Lot founded = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        FullLotDTO target =  FullLotDTO.fromLot(founded);
        target.setCurrentPrice(lotRepository.getCurrentPriceById(id));
        return target;
    }
    public List<FullLotDTO> getAllFullLots() {
        return lotRepository.findAll().stream()
                .map(FullLotDTO::fromLot)
                .peek(fullLotDTO -> fullLotDTO.setCurrentPrice(lotRepository.getCurrentPriceById(fullLotDTO.getId())))
                .collect(Collectors.toList());
    }

    public BidDTO getFirstBidPerson( Long id ) {
        Lot founded = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        return BidDTO.fromBid(founded.getBids().get(0));
    }

    public FrequentView getFrequentBid( Long id ) {
        Optional<Lot> founded = lotRepository.findById(id);
        if (founded.isEmpty()) {
            throw new LotNotFoundException();
        }
        return bidRepository.findFrequentOne(id);
    }

    public List<LotDTO> getAllLots( LotStatus status, Integer pageNumber ) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        List<LotDTO> needed = lotRepository.findAllByStatus(status, pageRequest).stream()
                .map(LotDTO::fromLot)
                .collect(Collectors.toList());

        if (needed.isEmpty()) {
            throw new LotNotFoundException();
        }
        return needed;
    }

    public void export( HttpServletResponse response ) throws IOException {
        List<FullLotDTO> lots = getAllFullLots();
        PrintWriter sw = response.getWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        lots
                .forEach(fullLotDTO -> {
                    try {
                        printer.printRecord(
                                fullLotDTO.getId(),
                                fullLotDTO.getStatus(),
                                fullLotDTO.getTitle(),
                                fullLotDTO.getDescription(),
                                fullLotDTO.getStartPrice(),
                                fullLotDTO.getBidPrice(),
                                fullLotDTO.getCurrentPrice(),
                                fullLotDTO.getLastBid().getBidderName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        printer.flush();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"lots.csv\"");

        PrintWriter pw = response.getWriter();
        pw.write(sw.toString());
        pw.flush();
        pw.close();
    }

    public String getStatusOfLot( Long id ) {
        Lot target = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (target.getStatus().equals(LotStatus.STARTED)) {
            return "started";
        }
        if (target.getStatus().equals(LotStatus.CREATED)) {
            return "created";
        }
        if (target.getStatus().equals(LotStatus.STOPPED)) {
            return "stopped";
        }
        return "none";
    }

}
