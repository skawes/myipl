CREATE DATABASE myipl;

--players table
CREATE TABLE players (
    id int auto_increment not null,
    p_name varchar(255),
    p_username varchar(255),
    p_password varchar(255),
    p_contact varchar(255),
    p_group_id int,
     PRIMARY KEY (id)
);
--ipl_group table
CREATE TABLE ipl_group (
    id int auto_increment not null,
    group_name varchar(255),
    `status` boolean default false,
     PRIMARY KEY (id)
);

--predictions table

CREATE TABLE predictions (
    id int auto_increment not null,
    p_username varchar(255),
    match1  varchar(255),
	match2  varchar(255),
     PRIMARY KEY (id)
);