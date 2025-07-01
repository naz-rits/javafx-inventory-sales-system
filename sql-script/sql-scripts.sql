CREATE TABLE IF NOT EXISTS product (
	product_id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_description VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    quantity BIGINT NOT NULL,
    low_stock_limit BIGINT NOT NULL,
    category VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sale (
	sale_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    date DATETIME NOT NULL DEFAULT current_timestamp,
    total_amount DECIMAL(10, 2) NOT NULL,
    customer_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sale_item(
	sale_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price FLOAT NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sale(sale_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);