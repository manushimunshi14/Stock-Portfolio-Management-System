use sql5666581;

DROP TABLE IF EXISTS purchasedstocks;

CREATE TABLE purchasedstocks(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    stock_id INT UNSIGNED NOT NULL,
    selling_count INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT sr_user_fk_purchasedstocks FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT sr_stock_fk_purchasedstocks FOREIGN KEY (stock_id) REFERENCES stocks(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
