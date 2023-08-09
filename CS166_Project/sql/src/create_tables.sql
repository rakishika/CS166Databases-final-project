DROP TABLE WORK_EXPR;
DROP TABLE EDUCATIONAL_DETAILS;
DROP TABLE MESSAGE;
DROP TABLE CONNECTION_USR;
DROP TABLE USR;

-- increased length of userId and password
-- added foreign keys
-- added MESSAGE_SEQ

CREATE TABLE USR(
userId varchar(30) UNIQUE NOT NULL,
password varchar(20) NOT NULL,
email text NOT NULL,
name char(50),
dateOfBirth date,
Primary Key(userId));

CREATE TABLE WORK_EXPR(
userId char(30) NOT NULL,
company char(50) NOT NULL,
role char(50) NOT NULL,
location char(50),
startDate date,
endDate date,
PRIMARY KEY(userId,company,role,startDate),
FOREIGN KEY (userId) references USR(userId));

CREATE TABLE EDUCATIONAL_DETAILS(
userId char(30) NOT NULL,
instituitionName char(50) NOT NULL,
major char(50) NOT NULL,
degree char(50) NOT NULL,
startdate date,
enddate date,
PRIMARY KEY(userId,major,degree),
FOREIGN KEY (userId) references USR(userId));

CREATE TABLE MESSAGE(
msgId integer UNIQUE NOT NULL,
senderId char(30) NOT NULL,
receiverId char(30) NOT NULL,
contents char(500) NOT NULL,
sendTime timestamp,
deleteStatus integer,
status char(30) NOT NULL,
PRIMARY KEY(msgId),
FOREIGN KEY (senderId) references USR(userId),
FOREIGN KEY (receiverId) references USR(userId));

CREATE TABLE CONNECTION_USR(
userId char(30) NOT NULL,
connectionId char(30) NOT NULL,
status char(30) NOT NULL,
PRIMARY KEY(userId,connectionId),
FOREIGN KEY (userId) references USR(userId),
FOREIGN KEY (connectionId) references USR(userId));

CREATE SEQUENCE MESSAGE_SEQ
start 27812
increment 1;
