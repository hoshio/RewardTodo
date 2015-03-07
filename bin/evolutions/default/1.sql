# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table todos (
  id                        bigint not null,
  todo_name                 varchar(255),
  todo_detail               varchar(255),
  author                    varchar(255),
  postdate                  timestamp not null,
  constraint pk_todos primary key (id))
;

create sequence todos_seq;




# --- !Downs

drop table if exists todos cascade;

drop sequence if exists todos_seq;

