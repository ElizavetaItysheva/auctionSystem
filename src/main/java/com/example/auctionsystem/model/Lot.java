package com.example.auctionsystem.model;

import com.example.auctionsystem.model.status.LotStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lots")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Long id;
    private String status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    @OneToMany(mappedBy = "lot")
    private List<Bid> bids;

    public Lot() {
    }
    public Integer getCurrentPrice(){
        if(this.getStatus().equals(LotStatus.CREATED.toString())){
            return startPrice;
        } else {
            return (bids.size() * bidPrice) + startPrice;
        }
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids( List<Bid> bids ) {
        this.bids = bids;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
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
}
