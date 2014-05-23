drop database if exists booksdb;

create database booksdb;


use booksdb;


create table users (
	
username	varchar(20) not null primary key,
	
userpass	char(32) not null,
	
name	varchar(70) not null,
	
email	varchar(255) not null
);



create table user_roles (
	
username	varchar(20) not null,
	
rolename 	varchar(20) not null,
	
foreign key(username) references users(username) on delete cascade,
	
primary key (username, rolename)
);

create table book (
idbook 		INTEGER AUTO_INCREMENT not null primary key,
tittle		varchar(50) not null,
author  	varchar(50) not null,
language 	varchar (20) not null,
edition		varchar (20),
dateedition date,
dateprint   date,
editorial   varchar (20) 

);

create table review (
idreview 	INTEGER unique auto_increment not null,
username 	varchar(20) not null,
dateupdate	timestamp not null,
text		varchar(500),
idbook 		INTEGER not null,

foreign key(username) references users(username),
foreign key(idbook) references book(idbook) on delete cascade,

primary key ( idbook, username)

);