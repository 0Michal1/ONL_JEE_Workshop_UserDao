CREATE DATABASE IF NOT EXISTS workshop
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE workshop;
CREATE TABLE IF NOT EXISTS users(
    id INT(11) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
    );

-- #     dodawanie użytkownika,
INSERT INTO users('email', 'username', password) VALUES(?,?,?);
-- #     zmiana danych,
UPDATE users SET password = ?  WHERE id = ?;
UPDATE users SET username = ?  WHERE id = ?;
UPDATE users SET email = ?  WHERE id = ?;
-- #     pobieranie po id,
SELECT * FROM users WHERE  id =?;
-- #     usuwanie po id,
DELETE FROM users WHERE id = ?;
-- #     pobieranie wszystkich użytkowników.
SELECT * FROM users;
-- pobranie ostatniego id
SELECT id FROM users WHERE email = ?;