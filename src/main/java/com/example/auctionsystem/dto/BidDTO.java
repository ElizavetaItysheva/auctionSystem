package com.example.auctionsystem.dto;

import com.example.auctionsystem.model.Bid;

public class BidDTO {
    private String bidderName;
    private String bidDate;
    public static BidDTO fromBid( Bid bid){
        BidDTO dto = new BidDTO();
        dto.setBidderName(bid.getBidderName());
        dto.setBidDate(bid.getBidDate());
        return dto;
    }
    public Bid toBid(){
        Bid bid = new Bid();
        bid.setBidderName(this.getBidderName());
        bid.setBidDate(this.getBidDate());
        return bid;
    }

    public BidDTO() {
    }
    public BidDTO(String bidderName, String bidDate){
        this.bidderName = bidderName;
        this.bidDate = bidDate;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName( String bidderName ) {
        this.bidderName = bidderName;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate( String bidDate ) {
        this.bidDate = bidDate;
    }
}
