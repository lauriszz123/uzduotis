CREATE SCHEMA IF NOT EXISTS uzduotis AUTHORIZATION sa;
 /*
  TABLE uzduotis.users

  id is a BIGINT because it is an ID, it needs to support a lot of users and unique ones.
  username is VARCHAR(20) because long usernames are not allowed.
  password is VARCHAR(20) because long password is hard to remember.
  is_admin is BOOLEAN because in this case it only stores if it is an important user or not.
  */
 CREATE TABLE IF NOT EXISTS uzduotis.users(
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     username VARCHAR(20) NOT NULL,
     password VARCHAR(20) NOT NULL,
     is_admin BOOLEAN NOT NULL
 );
 /*
  TABLE uzduotis.sessions

  id is a BIGINT because it is an ID, it needs to support a lot of users logged in and unique sessions.
  token is VARCHAR(36) because hashed value will always be 36 characters long.
  user_id is BIGINT it stores id's of logged in users for reference.
  timestamp is BIGINT because it stores a UNIX Timestamp value which is stored in memory as Long.
  */
 CREATE TABLE IF NOT EXISTS uzduotis.sessions(
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     token VARCHAR(36) NOT NULL,
     user_id BIGINT NOT NULL,
     timestamp BIGINT NOT NULL
 );
 /*
  TABLE uzduotis.messages

  id is a BIGINT because it is an ID, it needs to support a lot of users logged in and unique sessions.
  message is VARCHAR(255) it needs to store long messages written by users.
  user_id is BIGINT it stores id's of logged in users for reference.
  timestamp is BIGINT because it stores a UNIX Timestamp value which is stored in memory as Long.
  */
 CREATE TABLE IF NOT EXISTS uzduotis.messages(
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     message VARCHAR(255) NOT NULL,
     user_id BIGINT NOT NULL,
     timestamp BIGINT NOT NULL
 );
 /*
  This script will merge an admin user so that there
  would be one admin at all times, who can create more users using the API.
  */
 MERGE INTO uzduotis.users VALUES (0, 'admin', 'admin', true);
