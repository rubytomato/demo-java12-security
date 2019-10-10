CREATE DATABASE IF NOT EXISTS test_db
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
;

CREATE USER IF NOT EXISTS 'test_user'@'localhost'
  IDENTIFIED BY 'test_user'
  PASSWORD EXPIRE NEVER
;

GRANT ALL ON test_db.* TO 'test_user'@'localhost';
