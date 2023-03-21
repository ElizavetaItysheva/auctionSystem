-- liquibase formatted sql

-- changeset elizaveta:1
CREATE TABLE lots(
    lot_id BIGSERIAL NOT NULL PRIMARY KEY ,
    status VARCHAR(64) DEFAULT 'CREATED' not null ,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(4096) NOT NULL ,
    start_price INT NOT NULL ,
    bid_price INT NOT NULL
)