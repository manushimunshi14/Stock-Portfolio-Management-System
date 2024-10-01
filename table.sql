use sql5666581;

DROP TABLE IF EXISTS sellingrecord;
DROP TABLE IF EXISTS stockswallet;
DROP TABLE IF EXISTS wallet;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS password;
DROP TABLE IF EXISTS manager;

create table manager (
    id int unsigned not null auto_increment,
    manager boolean not null,
    primary key(id)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;

create table password (
    id int unsigned not null auto_increment,
    password longtext not null,
    primary key(id)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;

create table user (
    id int unsigned not null auto_increment,
    user_name varchar(255) not null,
    display_name varchar(255) not null,
    manager_id int unsigned not null,
    password_id int unsigned not null,
    primary key(id),
    constraint password_fk foreign key(password_id) references password(id),
    constraint manager_fk foreign key(manager_id) references manager(id)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_general_ci;

CREATE TABLE stockswallet (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    stock_id INT UNSIGNED NOT NULL,
    count INT UNSIGNED NOT NULL,
    transaction_price DECIMAL(10,2) NOT NULL,
    trading_date DATETIME NOT NULL,
    total_consumption DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT sw_user_fk_stockswallet FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT sw_stock_fk_stockswallet FOREIGN KEY (stock_id) REFERENCES stocks(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE wallet (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 999999.99,
    PRIMARY KEY (id),
    CONSTRAINT user_wallet_fk FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



INSERT INTO password (password) VALUES ('$31$16$sKeuG-vAIIIksFdS9NodZ8Lz7OhJ8vltF2Yjmjy7wJw');
INSERT INTO manager (manager) VALUES (true);



INSERT INTO user (user_name, display_name, manager_id, password_id)
VALUES ('admin', 'Big Bro', LAST_INSERT_ID(), (SELECT id FROM password ORDER BY id DESC LIMIT 1));


INSERT INTO wallet (user_id)
VALUES ((SELECT id FROM password ORDER BY id DESC LIMIT 1));

SET @admin_id = LAST_INSERT_ID();
SET @stock_count = 100;
SET @purchase_date = '2023-12-01 00:00:00';


INSERT INTO stockswallet (user_id, stock_id, count, transaction_price, trading_date, total_consumption)
VALUES
(@admin_id, 1, @stock_count, 190.87,@purchase_date, 19087.00),
(@admin_id, 2, @stock_count, 327.24, @purchase_date, 32724.00),
(@admin_id, 3, @stock_count, 468.62, @purchase_date, 46862.00),
(@admin_id, 4, @stock_count, 132.49, @purchase_date, 13249.00),
(@admin_id, 5, @stock_count, 48.66, @purchase_date, 4866.00);
