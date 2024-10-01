use sql5666581;

DROP TABLE IF EXISTS  request;
DROP TABLE IF EXISTS  derivative_user;

CREATE TABLE request(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_name varchar(255) not null,
    display_name varchar(255) not null,
    password longtext not null,
    derivative boolean not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE derivative_user(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED not null,
    derivative boolean not null,
    PRIMARY KEY (id),
    CONSTRAINT derivative_user_fk FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
