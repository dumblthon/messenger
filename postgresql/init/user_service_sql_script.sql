CREATE DATABASE user_service;
CREATE USER user_service WITH ENCRYPTED PASSWORD 'user_service';
GRANT ALL PRIVILEGES ON DATABASE user_service TO user_service;

