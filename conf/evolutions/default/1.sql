# Users schema
 
# --- !Ups
 
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username varchar(50) NOT NULL,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE users;
