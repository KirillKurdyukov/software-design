create table Product
(
    id    serial,
    name  text not null,
    price int  not null,

    primary key (id)
);