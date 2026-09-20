CREATE DATABASE IF NOT EXISTS springedu_eval CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'springedu_user'@'%' IDENTIFIED BY 'adminpass';
GRANT ALL PRIVILEGES ON springedu_eval.* TO 'springedu_user'@'%'; FLUSH PRIVILEGES;
