create table ListsWorks
(
    ListId   serial,
    ListName varchar(50),

    primary key (ListId)
);

create table Works
(
    WorkId   serial,
    ListId   int,
    WorkName varchar(50),
    IsReady  boolean default false,

    primary key (WorkId),

    foreign key (ListId) references ListsWorks (ListId) on delete cascade
);

create index find_by_list_id_index on Works using btree (ListId);