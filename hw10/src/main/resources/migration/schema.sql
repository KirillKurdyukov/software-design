create table season_tickets
(
    id      serial primary key,
    version int default 0,
    name    varchar(255)
);

create table events
(
    season_ticket_id int,
    version          int,
    created_at       timestamp default now(),
    event_type       varchar(255),
    data             text,

    primary key (season_ticket_id, version),

    foreign key (season_ticket_id) references season_tickets (id)
);
