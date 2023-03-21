package com.example.auctionsystem.dto;

import com.example.auctionsystem.model.Lot;

public class CreateLotDTO {
    //заголовок
    private String title;
    // описание
    private String description;
    // начальная цена
    private Integer startPrice;
    // цена ставки
    private Integer bidPrice;

    public CreateLotDTO() {
    }
    public static CreateLotDTO fromLot( Lot lot){
        CreateLotDTO createLot = new CreateLotDTO();
        createLot.setTitle(lot.getTitle());
        createLot.setDescription(lot.getDescription());
        createLot.setStartPrice(lot.getStartPrice());
        createLot.setBidPrice(lot.getBidPrice());
        return createLot;
    }
    public Lot toLot(){
        Lot lot = new Lot();
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());
        return lot;
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
}
