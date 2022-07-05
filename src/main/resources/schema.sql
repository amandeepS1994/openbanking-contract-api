drop table if exists transaction;

create table transaction (
    id bigint not null,
    account_number integer,
    amount double precision,
    currency varchar(255),
    date datetime,
    merchant_logo varchar(255),
    merchant_name varchar(255),
    type varchar(255),
    primary key (id)
) engine=InnoDB;