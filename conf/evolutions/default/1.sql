# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table rewards (
  id                        bigint not null,
  reward_name               varchar(255),
  reward_detail             varchar(255),
  username                  varchar(255),
  point                     varchar(255),
  status                    varchar(255),
  postdate                  timestamp not null,
  constraint pk_rewards primary key (id))
;

create table todos (
  id                        bigint not null,
  todo_name                 varchar(255),
  todo_detail               varchar(255),
  username                  varchar(255),
  point                     varchar(255),
  status                    varchar(255),
  limitdata                 varchar(255),
  postdate                  timestamp not null,
  constraint pk_todos primary key (id))
;

create table users (
  id                        bigint not null,
  username                  varchar(255),
  sum_point                 varchar(255),
  postdate                  timestamp not null,
  constraint pk_users primary key (id))
;

create sequence rewards_seq;

create sequence todos_seq;

create sequence users_seq;




# --- !Downs

drop table if exists rewards cascade;

drop table if exists todos cascade;

drop table if exists users cascade;

drop sequence if exists rewards_seq;

drop sequence if exists todos_seq;

drop sequence if exists users_seq;

