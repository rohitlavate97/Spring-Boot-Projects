show databases;

create database sbms;

use sbms;

show tables;

-----truncate book;
-- drop table book;
-- drop table book_seq

select * from book;

select * from book_seq;

select * from hibernate_sequences

select * from employee;

-- CREATE SEQUENCE ORDER_ID_SEQ
-- START WITH 1
-- INCREMENT BY 1;

select * from account_tbl

select * from student_enquiries

create database devDb;

create database stageDb;

create database prodDb;

select * from product

CREATE TABLE `users` (
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(120) NOT NULL,
  `enabled` TINYINT(1) NOT NULL,
  PRIMARY KEY (`username`)
);

select * from users;

-- drop table users;
-- drop table authorities;

CREATE TABLE `authorities` (
  `username` VARCHAR(50) NOT NULL,
  `authority` VARCHAR(50) NOT NULL,
  KEY `username` (`username`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`)
    REFERENCES `users` (`username`)
);

select * from authorities;

insert into users values('admin','$2a$12$LlOKsem5HNKzdI.xjycFRebyRjzdIhVrdXuehk8du7272j1ILl1Ki',1);     <----decrypt --password=admin@123--->
insert into users values('user','$2a$12$9Y5BFUvctEQj5jPtAX5NIuH1AUr0AROiklwjUjuI8yC0d1O2d3nQC',1);

insert into authorities values('admin','ROLE_ADMIN');
insert into authorities values('admin','ROLE_USER');
insert into authorities values('user','ROLE_USER');



commit;
