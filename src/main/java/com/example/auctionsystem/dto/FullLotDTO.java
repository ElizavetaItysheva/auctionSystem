package com.example.auctionsystem.dto;

import com.example.auctionsystem.model.Lot;
import com.example.auctionsystem.model.status.LotStatus;

public class FullLotDTO {
    private Long id;
    private LotStatus status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private BidDTO lastBid;

    public FullLotDTO() {
    }
    public static FullLotDTO fromLot( Lot lot){
        FullLotDTO dto = new FullLotDTO();
        dto.setId(lot.getId());
        dto.setStatus(lot.getStatus());
        dto.setTitle(lot.getTitle());
        dto.setDescription(lot.getDescription());
        dto.setStartPrice(lot.getStartPrice());
        dto.setBidPrice(lot.getBidPrice());
        dto.setLastBid(lot.getLastBid());
        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public LotStatus getStatus() {
        return status;
    }

    public void setStatus( LotStatus status ) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Integer getStartPrice() {
        return startPrice;
    }

    public void setStartPrice( Integer startPrice ) {
        this.startPrice = startPrice;
    }

    public Integer getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice( Integer bidPrice ) {
        this.bidPrice = bidPrice;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice( Integer currentPrice ) {
        this.currentPrice = currentPrice;
    }

    public BidDTO getLastBid() {
        return lastBid;
    }

    public void setLastBid( BidDTO lastBid ) {
        this.lastBid = lastBid;
    }
}
