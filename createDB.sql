
--drop schema loginjsf;

create schema loginjsf;

CREATE TABLE loginjsf.Users( 
    uid int(20) NOT NULL AUTO_INCREMENT, 
    uname VARCHAR(60) NOT NULL, 
    password VARCHAR(60) NOT NULL, 
    roles VARCHAR(60) NOT NULL, 
    PRIMARY KEY(uid)
);

INSERT INTO loginjsf.Users VALUES(null, 'admin','123', 'ADMIN');
INSERT INTO loginjsf.Users VALUES(null, 'client','123', 'CLIENT');
INSERT INTO loginjsf.Users VALUES(null, 'user','123', 'USER');
INSERT INTO loginjsf.Users VALUES(null, 'all','123', 'ADMIN,CLIENT,USER');
