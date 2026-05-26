drop database if exists ManagePlayer;
create database ManagePlayer;
use ManagePlayer;

drop table if exists team;


create table team(
     id int primary key auto_increment,
     teamName varchar(20) unique ,
     password varchar(20),
     photoPath varchar(1024),
     introduce mediumtext,
     playerCount int
);

drop table if exists players;

create table players(
     id int primary key auto_increment,
     playerName varchar(1024),
     postTime datetime,
     content mediumtext,
     userId int
);

drop table if exists reviews;

create table reviews(id int primary key auto_increment,
                     postTime datetime,
                     content mediumtext,
                     postAuthor varchar(1024),
                     playerId int
);