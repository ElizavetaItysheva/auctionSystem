-- liquibase formatted sql

-- changeset elizaveta:2
CREATE TABLE bids(
    bid_id BIGSERIAL NOT NULL PRIMARY KEY ,
    bidder_name VARCHAR(255) NOT NULL ,
    bid_date VARCHAR(255),
    lot_lot_id bigint references lots(lot_id)
)