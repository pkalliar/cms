# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contact (
  id                        bigint not null,
  name                      varchar(255),
  title                     varchar(255),
  email                     varchar(255),
  constraint pk_contact primary key (id))
;

create table folder (
  id                        bigint not null,
  name                      varchar(255),
  code                      varchar(255),
  constraint pk_folder primary key (id))
;

create table folder_category (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_folder_category primary key (id))
;

create table incoming_doc (
  id                        bigint not null,
  protocolnumber            varchar(255),
  name                      varchar(255),
  sender                    varchar(255),
  folder_id                 bigint,
  medium_id                 bigint,
  location                  varchar(255),
  receivedon                timestamp,
  constraint pk_incoming_doc primary key (id))
;

create table protocol (
  id                        bigint not null,
  alfasigma                 integer,
  name                      varchar(255),
  creator_username          varchar(255),
  folder_id                 bigint,
  createdon                 timestamp,
  constraint pk_protocol primary key (id))
;

create table task (
  id                        bigint not null,
  label                     varchar(255),
  constraint pk_task primary key (id))
;

create table transmission_medium (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_transmission_medium primary key (id))
;

create table account (
  username                  varchar(255) not null,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (username))
;

create sequence contact_seq;

create sequence folder_seq;

create sequence folder_category_seq;

create sequence incoming_doc_seq;

create sequence protocol_seq;

create sequence task_seq;

create sequence transmission_medium_seq;

create sequence account_seq;

alter table incoming_doc add constraint fk_incoming_doc_folder_1 foreign key (folder_id) references folder (id) on delete restrict on update restrict;
create index ix_incoming_doc_folder_1 on incoming_doc (folder_id);
alter table incoming_doc add constraint fk_incoming_doc_medium_2 foreign key (medium_id) references transmission_medium (id) on delete restrict on update restrict;
create index ix_incoming_doc_medium_2 on incoming_doc (medium_id);
alter table protocol add constraint fk_protocol_creator_3 foreign key (creator_username) references account (username) on delete restrict on update restrict;
create index ix_protocol_creator_3 on protocol (creator_username);
alter table protocol add constraint fk_protocol_folder_4 foreign key (folder_id) references folder (id) on delete restrict on update restrict;
create index ix_protocol_folder_4 on protocol (folder_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contact;

drop table if exists folder;

drop table if exists folder_category;

drop table if exists incoming_doc;

drop table if exists protocol;

drop table if exists task;

drop table if exists transmission_medium;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contact_seq;

drop sequence if exists folder_seq;

drop sequence if exists folder_category_seq;

drop sequence if exists incoming_doc_seq;

drop sequence if exists protocol_seq;

drop sequence if exists task_seq;

drop sequence if exists transmission_medium_seq;

drop sequence if exists account_seq;

