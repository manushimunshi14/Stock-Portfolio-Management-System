USE sql5666581;

DROP TABLE IF EXISTS stockdb;
DROP TABLE IF EXISTS stocks;


CREATE TABLE stocks
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE stockMarket
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    stock_id INT UNSIGNED NOT NULL,
    last_sale DECIMAL(10,2) NOT NULL,
    last_update_time DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT stock_fk FOREIGN KEY (stock_id) REFERENCES stocks(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO stocks (symbol, name) VALUES ('AAPL', 'Apple Inc. Common Stock');

INSERT INTO stocks (symbol, name) VALUES ('MSFT', 'Microsoft Corporation Common Stock');

INSERT INTO stocks (symbol, name) VALUES ('NVDA', 'NVIDIA Corporation Common Stock');

INSERT INTO stocks (symbol, name) VALUES ('GOOG', 'Alphabet Inc. Class C Capital Stock');

INSERT INTO stocks (symbol, name) VALUES ('CSCO', 'Cisco Systems, Inc. Common Stock (DE)');


INSERT INTO stockMarket (stock_id, last_sale,  last_update_time) VALUES (1, 190.87, '2023-12-01 00:00:00');

INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (2, 372.24, '2023-12-01 00:00:00');

INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (3, 468.62, '2023-12-01 00:00:00');

INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (4, 132.49, '2023-12-01 00:00:00');

INSERT INTO stockMarket (stock_id, last_sale, last_update_time) VALUES (5, 48.66, '2023-12-01 00:00:00');

