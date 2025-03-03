CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(255),
    full_name VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY(customer_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id VARCHAR(255),
    asset_name VARCHAR(255),
    order_side VARCHAR(50),
    size DOUBLE,
    price DOUBLE,
    status VARCHAR(50),
    create_date DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS assets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id VARCHAR(255),
    asset_name VARCHAR(255),
    size DOUBLE,
    usable_size DOUBLE,
    PRIMARY KEY (id),
    UNIQUE (customer_id, asset_name)

);
