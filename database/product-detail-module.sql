USE myntrademo_db;

SET SQL_SAFE_UPDATES = 0;

CREATE TABLE IF NOT EXISTS product_seller_details (
    seller_detail_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    seller_name VARCHAR(150) NOT NULL,
    delivery_min_days INT NOT NULL DEFAULT 2,
    delivery_max_days INT NOT NULL DEFAULT 5,
    return_policy VARCHAR(180) DEFAULT NULL,
    exchange_policy VARCHAR(180) DEFAULT NULL,
    cod_available TINYINT(1) NOT NULL DEFAULT 1,
    original_product_text VARCHAR(180) DEFAULT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (seller_detail_id),
    UNIQUE KEY uq_product_seller_details_product (product_id),
    CONSTRAINT fk_product_seller_details_product
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_product_seller_delivery_days
        CHECK (delivery_min_days >= 0 AND delivery_max_days >= delivery_min_days)
);

CREATE TABLE IF NOT EXISTS product_offers (
    offer_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT DEFAULT NULL,
    offer_title VARCHAR(180) NOT NULL,
    offer_description VARCHAR(500) DEFAULT NULL,
    terms_text VARCHAR(180) DEFAULT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    start_date TIMESTAMP NULL DEFAULT NULL,
    end_date TIMESTAMP NULL DEFAULT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (offer_id),
    KEY idx_product_offers_product (product_id),
    KEY idx_product_offers_active_dates (is_active, start_date, end_date),
    CONSTRAINT fk_product_offers_product
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS product_service_promises (
    promise_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT DEFAULT NULL,
    promise_title VARCHAR(180) NOT NULL,
    promise_subtitle VARCHAR(255) DEFAULT NULL,
    icon_key VARCHAR(50) DEFAULT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (promise_id),
    KEY idx_product_service_promises_product (product_id),
    KEY idx_product_service_promises_active (is_active),
    CONSTRAINT fk_product_service_promises_product
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

DELETE FROM product_specifications
WHERE product_id IN (
    SELECT product_id FROM products WHERE slug IN (
        'classic-formal-shirt',
        'printed-cotton-kurta',
        'active-running-sneakers',
        'washed-denim-jacket',
        'slim-fit-jeans',
        'everyday-cotton-tshirt',
        'kids-comfort-hoodie',
        'soft-home-cushion',
        'hydrating-beauty-serum',
        'urban-travel-backpack'
    )
);

INSERT INTO product_specifications (product_id, spec_name, spec_value, sort_order)
SELECT p.product_id, spec.spec_name, spec.spec_value, spec.sort_order
FROM products p
JOIN (
    SELECT 'classic-formal-shirt' AS slug, 'Fit' AS spec_name, 'Regular Fit' AS spec_value, 1 AS sort_order UNION ALL
    SELECT 'classic-formal-shirt', 'Fabric', 'Cotton Blend', 2 UNION ALL
    SELECT 'classic-formal-shirt', 'Collar', 'Spread Collar', 3 UNION ALL
    SELECT 'classic-formal-shirt', 'Sleeve Length', 'Long Sleeves', 4 UNION ALL
    SELECT 'classic-formal-shirt', 'Occasion', 'Formal', 5 UNION ALL

    SELECT 'printed-cotton-kurta', 'Fabric', 'Cotton', 1 UNION ALL
    SELECT 'printed-cotton-kurta', 'Pattern', 'Printed', 2 UNION ALL
    SELECT 'printed-cotton-kurta', 'Sleeve Length', 'Three-Quarter Sleeves', 3 UNION ALL
    SELECT 'printed-cotton-kurta', 'Occasion', 'Ethnic', 4 UNION ALL

    SELECT 'active-running-sneakers', 'Material', 'Mesh Upper', 1 UNION ALL
    SELECT 'active-running-sneakers', 'Sole Material', 'Rubber', 2 UNION ALL
    SELECT 'active-running-sneakers', 'Closure', 'Lace-Up', 3 UNION ALL
    SELECT 'active-running-sneakers', 'Sport', 'Running', 4 UNION ALL

    SELECT 'washed-denim-jacket', 'Fabric', 'Denim', 1 UNION ALL
    SELECT 'washed-denim-jacket', 'Pattern', 'Washed', 2 UNION ALL
    SELECT 'washed-denim-jacket', 'Sleeve Length', 'Long Sleeves', 3 UNION ALL
    SELECT 'washed-denim-jacket', 'Closure', 'Button', 4 UNION ALL

    SELECT 'slim-fit-jeans', 'Fit', 'Slim Fit', 1 UNION ALL
    SELECT 'slim-fit-jeans', 'Waist Rise', 'Mid-Rise', 2 UNION ALL
    SELECT 'slim-fit-jeans', 'Length', 'Regular', 3 UNION ALL
    SELECT 'slim-fit-jeans', 'Stretch', 'Stretchable', 4 UNION ALL

    SELECT 'everyday-cotton-tshirt', 'Fabric', 'Cotton', 1 UNION ALL
    SELECT 'everyday-cotton-tshirt', 'Pattern', 'Solid', 2 UNION ALL
    SELECT 'everyday-cotton-tshirt', 'Neck', 'Round Neck', 3 UNION ALL
    SELECT 'everyday-cotton-tshirt', 'Sleeve Length', 'Short Sleeves', 4 UNION ALL

    SELECT 'kids-comfort-hoodie', 'Fabric', 'Cotton Fleece', 1 UNION ALL
    SELECT 'kids-comfort-hoodie', 'Closure', 'Slip-On', 2 UNION ALL
    SELECT 'kids-comfort-hoodie', 'Pattern', 'Solid', 3 UNION ALL
    SELECT 'kids-comfort-hoodie', 'Occasion', 'Casual', 4 UNION ALL

    SELECT 'soft-home-cushion', 'Fabric', 'Cotton Blend', 1 UNION ALL
    SELECT 'soft-home-cushion', 'Shape', 'Square', 2 UNION ALL
    SELECT 'soft-home-cushion', 'Pattern', 'Textured', 3 UNION ALL
    SELECT 'soft-home-cushion', 'Care', 'Gentle Wash', 4 UNION ALL

    SELECT 'hydrating-beauty-serum', 'Formulation', 'Serum', 1 UNION ALL
    SELECT 'hydrating-beauty-serum', 'Skin Type', 'All Skin Types', 2 UNION ALL
    SELECT 'hydrating-beauty-serum', 'Preference', 'Cruelty-Free', 3 UNION ALL
    SELECT 'hydrating-beauty-serum', 'Concern', 'Hydration', 4 UNION ALL

    SELECT 'urban-travel-backpack', 'Material', 'Polyester', 1 UNION ALL
    SELECT 'urban-travel-backpack', 'Closure', 'Zip', 2 UNION ALL
    SELECT 'urban-travel-backpack', 'Compartments', '3', 3 UNION ALL
    SELECT 'urban-travel-backpack', 'Capacity', 'Medium', 4
) spec ON p.slug = spec.slug;

DELETE FROM product_seller_details
WHERE product_id IN (SELECT product_id FROM products);

INSERT INTO product_seller_details
(product_id, seller_name, delivery_min_days, delivery_max_days, return_policy, exchange_policy, cod_available, original_product_text, is_active)
SELECT
    p.product_id,
    CONCAT(b.brand_name, ' Authorized Seller'),
    2,
    5,
    'Easy return and exchange available',
    'Exchange available for eligible sizes',
    TRUE,
    '100% Original Products',
    TRUE
FROM products p
INNER JOIN brands b ON p.brand_id = b.brand_id
WHERE p.product_status = 'ACTIVE';

DELETE FROM product_service_promises;

INSERT INTO product_service_promises (product_id, promise_title, promise_subtitle, icon_key, sort_order, is_active)
VALUES
(NULL, 'Secure checkout', 'Protected shopping and user-owned cart records', 'secure', 1, TRUE),
(NULL, 'Original product assurance', 'Seller and product promise is stored in database', 'original', 2, TRUE),
(NULL, 'Easy shopping support', 'Delivery and return rules are managed from product records', 'support', 3, TRUE);

DELETE FROM product_offers;

INSERT INTO product_offers (product_id, offer_title, offer_description, terms_text, sort_order, is_active, start_date, end_date)
VALUES
(NULL, '10% Instant Discount on selected bank cards', 'Applicable on eligible cart value during checkout.', 'Terms & Condition', 1, TRUE, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 365 DAY)),
(NULL, 'Additional catalogue offer', 'Unlock additional offer by buying from selected catalogue.', 'View Products', 2, TRUE, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 365 DAY));

INSERT INTO users (role_id, full_name, email, phone, password_hash, is_active, is_email_verified, is_phone_verified)
SELECT role_id, 'Aarav Sharma', 'reviewer.aarav@myntrademo.local', NULL, '$2a$12$Wc7bHQeRCY/0Qjy7VqYApuT9x2A3PFIhSkFT0eTn6F93HwtbRJy5q', TRUE, TRUE, FALSE
FROM roles WHERE role_name = 'CUSTOMER'
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), is_active = TRUE;

INSERT INTO users (role_id, full_name, email, phone, password_hash, is_active, is_email_verified, is_phone_verified)
SELECT role_id, 'Meera Nair', 'reviewer.meera@myntrademo.local', NULL, '$2a$12$Wc7bHQeRCY/0Qjy7VqYApuT9x2A3PFIhSkFT0eTn6F93HwtbRJy5q', TRUE, TRUE, FALSE
FROM roles WHERE role_name = 'CUSTOMER'
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), is_active = TRUE;

INSERT INTO users (role_id, full_name, email, phone, password_hash, is_active, is_email_verified, is_phone_verified)
SELECT role_id, 'Dinesh Kumar', 'reviewer.dinesh@myntrademo.local', NULL, '$2a$12$Wc7bHQeRCY/0Qjy7VqYApuT9x2A3PFIhSkFT0eTn6F93HwtbRJy5q', TRUE, TRUE, FALSE
FROM roles WHERE role_name = 'CUSTOMER'
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), is_active = TRUE;

INSERT INTO product_reviews (product_id, user_id, rating, review_title, review_text, is_verified_purchase, is_approved)
SELECT p.product_id, u.user_id, 5, 'Good quality product', CONCAT(p.product_name, ' looks premium and feels comfortable for daily use.'), TRUE, TRUE
FROM products p
INNER JOIN users u ON u.email = 'reviewer.aarav@myntrademo.local'
WHERE p.product_status = 'ACTIVE'
ON DUPLICATE KEY UPDATE
rating = VALUES(rating),
review_title = VALUES(review_title),
review_text = VALUES(review_text),
is_verified_purchase = VALUES(is_verified_purchase),
is_approved = VALUES(is_approved);

INSERT INTO product_reviews (product_id, user_id, rating, review_title, review_text, is_verified_purchase, is_approved)
SELECT p.product_id, u.user_id, 4, 'Worth the price', CONCAT('The ', p.product_name, ' has a clean finish and the product matched the catalog image.'), TRUE, TRUE
FROM products p
INNER JOIN users u ON u.email = 'reviewer.meera@myntrademo.local'
WHERE p.product_status = 'ACTIVE'
ON DUPLICATE KEY UPDATE
rating = VALUES(rating),
review_title = VALUES(review_title),
review_text = VALUES(review_text),
is_verified_purchase = VALUES(is_verified_purchase),
is_approved = VALUES(is_approved);

INSERT INTO product_reviews (product_id, user_id, rating, review_title, review_text, is_verified_purchase, is_approved)
SELECT p.product_id, u.user_id, 5, 'Recommended', CONCAT('The product quality of ', p.product_name, ' is reliable and suitable for regular shopping needs.'), TRUE, TRUE
FROM products p
INNER JOIN users u ON u.email = 'reviewer.dinesh@myntrademo.local'
WHERE p.product_status = 'ACTIVE'
ON DUPLICATE KEY UPDATE
rating = VALUES(rating),
review_title = VALUES(review_title),
review_text = VALUES(review_text),
is_verified_purchase = VALUES(is_verified_purchase),
is_approved = VALUES(is_approved);

SET SQL_SAFE_UPDATES = 1;
