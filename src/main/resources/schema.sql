CREATE DATABASE IF NOT EXISTS rental_point;
USE rental_point;

-- Drop the child tables first (tables with foreign keys referencing other tables)
DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS product_instances;
DROP TABLE IF EXISTS products;

-- TABLE PRODUCTS
CREATE TABLE products
(
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255)   NOT NULL,
    description  TEXT,
    price        DECIMAL(10, 2) NOT NULL
);

-- TABLE PRODUCTS INSTANCE
CREATE TABLE product_instances
(
    instance_id  INT AUTO_INCREMENT PRIMARY KEY,
    product_id   INT NOT NULL,
    rental_price DECIMAL(10, 2),
    rental_rate  INT UNSIGNED DEFAULT 100,
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
);

-- TABLE CUSTOMERS
CREATE TABLE customers
(
    customer_id     INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    passport_number VARCHAR(20)  NOT NULL UNIQUE,
    phone_number    VARCHAR(15)  NOT NULL UNIQUE,
    rental_status   INT DEFAULT 0
);

-- TABLE RENTALS
CREATE TABLE rentals
(
    rental_id    INT AUTO_INCREMENT PRIMARY KEY,
    instance_id  INT            NOT NULL,
    customer_id  INT            NOT NULL,
    rental_date  DATE           NOT NULL,
    return_date  DATE           NOT NULL,
    rental_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (instance_id) REFERENCES product_instances (instance_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE CASCADE
);

-- TRIGGER REDUCE INSTANCE PRICE
DELIMITER $$

CREATE TRIGGER reduce_instance_price
    AFTER INSERT
    ON rentals
    FOR EACH ROW
BEGIN

    UPDATE product_instances
    SET rental_price = rental_price * 0.99,
        rental_rate  = rental_rate - 1
    WHERE instance_id = NEW.instance_id;
END$$

DELIMITER ;

-- UPDATE RENTAL STATUS
DELIMITER $$

CREATE TRIGGER update_rental_status
    AFTER INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE unique_rental_dates INT;

    SELECT COUNT(DISTINCT rental_date)
    INTO unique_rental_dates
    FROM rentals
    WHERE customer_id = NEW.customer_id;

    UPDATE customers
    SET rental_status = unique_rental_dates
    WHERE customer_id = NEW.customer_id;

    IF unique_rental_dates >= 5 THEN

        UPDATE customers
        SET rental_status = unique_rental_dates
        WHERE customer_id = NEW.customer_id;

    END IF;
END$$

DELIMITER ;


-- CALCULATE RENTAL PRICE
DELIMITER $$

CREATE TRIGGER calculate_rental_price
    BEFORE INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE current_price DECIMAL(10, 2);
    DECLARE discount_factor DECIMAL(10, 2);
    DECLARE days_rented INT;


    SELECT rental_price
    INTO current_price
    FROM product_instances
    WHERE instance_id = NEW.instance_id;

    SET days_rented = DATEDIFF(NEW.return_date, NEW.rental_date);

    IF days_rented <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Return date must be after rental date.';
    END IF;

    IF (SELECT rental_status FROM customers WHERE customer_id = NEW.customer_id) >= 5 THEN
        SET discount_factor = 0.95;
    ELSE
        SET discount_factor = 1.00;
    END IF;

    SET NEW.rental_price = current_price * days_rented * discount_factor;
END$$

DELIMITER ;


-- UPDATE INSTANCE PRICE
DELIMITER $$

CREATE TRIGGER update_instance_price_after_rental
    AFTER INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE new_price DECIMAL(10, 2);

    SELECT rental_price
    INTO new_price
    FROM product_instances
    WHERE instance_id = NEW.instance_id;

    SET new_price = new_price * 0.99;

    UPDATE product_instances
    SET rental_price = new_price
    WHERE instance_id = NEW.instance_id;
END$$

DELIMITER ;


-- CALCULATE INSTANCE PRICE
DELIMITER $$

CREATE TRIGGER calculate_instance_price
    BEFORE INSERT
    ON product_instances
    FOR EACH ROW
BEGIN
    DECLARE base_price DECIMAL(10, 2);


    SELECT price
    INTO base_price
    FROM products
    WHERE product_id = NEW.product_id;


    SET NEW.rental_price = base_price * 0.005;
END$$

DELIMITER ;

-- LIMITED RENTAL ITEMS
DELIMITER $$

CREATE TRIGGER limit_rented_items
    BEFORE INSERT
    ON rentals
    FOR EACH ROW
BEGIN
    DECLARE item_count INT;

    SELECT COUNT(*)
    INTO item_count
    FROM rentals
    WHERE customer_id = NEW.customer_id
      AND rental_date = NEW.rental_date;

    IF item_count >= 3 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Client cannot rent more than 3 items simultaneously.';
    END IF;
END$$

DELIMITER ;


-- INSERTION PRODUCTS
INSERT INTO products (product_name, description, price)
VALUES ('Bicycle', 'Mountain bicycle', 2000),
       ('Electric Scooter', 'Eco-friendly electric scooter', 1800),
       ('Skateboard', 'Classic wooden skateboard', 1400),
       ('Roller Skates', 'Inline roller skates', 1100),
       ('Hoverboard', 'Self-balancing hoverboard', 1200),
       ('Electric Bike', 'Electric-assisted bicycle', 2400);

-- INSERTION CUSTOMERS
INSERT INTO customers (first_name, last_name, passport_number, phone_number)
VALUES ('John', 'Doe', '123456781', '4440001234'),
       ('Jane', 'Smith', '765432132', '5550005678'),
       ('Mike', 'Johnson', '112233431', '5550008765'),
       ('Emily', 'Davis', '443322142', '5550004321'),
       ('David', 'Wilson', '556677852', '5550006789'),
       ('Sophia', 'Brown', '998877662', '5550008767'),
       ('Chris', 'Taylor', '223344521', '5550003456'),
       ('Olivia', 'Jones', '667788932', '5550005432'),
       ('James', 'Garcia', '887766545', '5550006543'),
       ('Lily', 'Martinez', '554433212', '5550007654');

-- INSERTION PRODUCT INSTANCE
INSERT INTO product_instances (product_id)
VALUES (1),
       (1),
       (1),
       (2),
       (2),
       (2),
       (3),
       (3),
       (3),
       (4),
       (4),
       (4),
       (5),
       (5),
       (5),
       (6),
       (6),
       (6);








