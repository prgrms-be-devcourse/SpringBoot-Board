create table post (
                      id bigint generated by default as identity,
                      created_at timestamp,
                      content clob not null,
                      title varchar(255) not null,
                      user_id bigint not null,
                      primary key (id)
);

create table user (
                      id bigint generated by default as identity,
                      created_at timestamp,
                      age integer not null,
                      hobby varchar(255) not null,
                      name varchar(255) not null,
                      primary key (id)
);

alter table post
    add constraint FK72mt33dhhs48hf9gcqrq4fxte
    foreign key (user_id)
    references user;