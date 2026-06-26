USE myntrademo_db;

SET SQL_SAFE_UPDATES = 0;

DELETE pi
FROM product_images pi
INNER JOIN products p ON pi.product_id = p.product_id
WHERE p.slug IN (
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
);

DELETE pv
FROM product_variants pv
INNER JOIN products p ON pv.product_id = p.product_id
WHERE p.slug IN (
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
);

DELETE FROM products
WHERE slug IN (
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
);

INSERT INTO categories (category_name, slug, description, image_url, is_active, sort_order)
VALUES
('Men', 'men', 'Men fashion collection', NULL, TRUE, 1),
('Women', 'women', 'Women fashion collection', NULL, TRUE, 2),
('Kids', 'kids', 'Kids fashion collection', NULL, TRUE, 3),
('Home', 'home', 'Home essentials collection', NULL, TRUE, 4),
('Beauty', 'beauty', 'Beauty and personal care collection', NULL, TRUE, 5),
('Footwear', 'footwear', 'Footwear collection', NULL, TRUE, 6),
('Accessories', 'accessories', 'Accessories collection', NULL, TRUE, 7)
ON DUPLICATE KEY UPDATE
category_name = VALUES(category_name),
description = VALUES(description),
is_active = VALUES(is_active),
sort_order = VALUES(sort_order);

INSERT INTO brands (brand_name, slug, description, logo_url, is_active)
VALUES
('UrbanThread', 'urbanthread', 'Modern everyday fashion brand', NULL, TRUE),
('ModaVista', 'modavista', 'Premium fashion essentials brand', NULL, TRUE),
('SoleCraft', 'solecraft', 'Comfort footwear brand', NULL, TRUE),
('HomeLuxe', 'homeluxe', 'Home lifestyle brand', NULL, TRUE),
('GlowCare', 'glowcare', 'Beauty and skincare brand', NULL, TRUE),
('BagWorks', 'bagworks', 'Travel and everyday bag brand', NULL, TRUE)
ON DUPLICATE KEY UPDATE
brand_name = VALUES(brand_name),
description = VALUES(description),
is_active = VALUES(is_active);

INSERT INTO products
(category_id, brand_id, product_name, slug, short_description, long_description, base_price, selling_price, discount_percent, product_status, is_featured)
VALUES
((SELECT category_id FROM categories WHERE slug = 'men'), (SELECT brand_id FROM brands WHERE slug = 'modavista'),
'Classic Formal Shirt', 'classic-formal-shirt', 'Slim formal shirt for everyday office wear.',
'A clean formal shirt designed for comfortable daily wear with a polished look.', 1499.00, 899.00, 40.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug = 'women'), (SELECT brand_id FROM brands WHERE slug = 'urbanthread'),
'Printed Cotton Kurta', 'printed-cotton-kurta', 'Soft printed kurta with regular fit.',
'A breathable cotton kurta designed for effortless daily styling.', 1999.00, 1199.00, 40.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug = 'footwear'), (SELECT brand_id FROM brands WHERE slug = 'solecraft'),
'Active Running Sneakers', 'active-running-sneakers', 'Lightweight sneakers for daily movement.',
'Comfortable running sneakers with lightweight support and clean styling.', 2999.00, 1899.00, 37.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug = 'men'), (SELECT brand_id FROM brands WHERE slug = 'urbanthread'),
'Washed Denim Jacket', 'washed-denim-jacket', 'Classic washed denim jacket.',
'A versatile denim jacket made for layering and everyday casual outfits.', 3499.00, 2499.00, 29.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug = 'men'), (SELECT brand_id FROM brands WHERE slug = 'modavista'),
'Slim Fit Jeans', 'slim-fit-jeans', 'Stretchable slim fit jeans.',
'Comfortable slim fit jeans with modern shape and everyday durability.', 2299.00, 1499.00, 35.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug = 'men'), (SELECT brand_id FROM brands WHERE slug = 'urbanthread'),
'Everyday Cotton T-Shirt', 'everyday-cotton-tshirt', 'Soft cotton crew neck t-shirt.',
'A simple cotton t-shirt made for daily wear and easy styling.', 899.00, 499.00, 44.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug = 'kids'), (SELECT brand_id FROM brands WHERE slug = 'urbanthread'),
'Kids Comfort Hoodie', 'kids-comfort-hoodie', 'Soft hoodie for kids.',
'A warm and comfortable hoodie suitable for casual kids wear.', 1599.00, 999.00, 38.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug = 'home'), (SELECT brand_id FROM brands WHERE slug = 'homeluxe'),
'Soft Home Cushion', 'soft-home-cushion', 'Decorative soft cushion.',
'A premium soft cushion designed to add comfort and style to your living space.', 799.00, 499.00, 38.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug = 'beauty'), (SELECT brand_id FROM brands WHERE slug = 'glowcare'),
'Hydrating Beauty Serum', 'hydrating-beauty-serum', 'Lightweight hydrating serum.',
'A gentle hydrating serum for everyday skincare routine.', 1299.00, 799.00, 38.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug = 'accessories'), (SELECT brand_id FROM brands WHERE slug = 'bagworks'),
'Urban Travel Backpack', 'urban-travel-backpack', 'Spacious everyday backpack.',
'A clean and functional backpack for college, work, and short travel.', 2499.00, 1699.00, 32.00, 'ACTIVE', TRUE);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', product_id, '-STD'), 'M', 'Default', base_price, selling_price, 50, 5, TRUE
FROM products
WHERE slug IN (
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
);

INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order)
VALUES
((SELECT product_id FROM products WHERE slug = 'classic-formal-shirt'), '/assets/images/products/classic-shirt.svg', 'Classic Formal Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'printed-cotton-kurta'), '/assets/images/products/cotton-kurta.svg', 'Printed Cotton Kurta', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'active-running-sneakers'), '/assets/images/products/running-sneakers.svg', 'Active Running Sneakers', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'washed-denim-jacket'), '/assets/images/products/denim-jacket.svg', 'Washed Denim Jacket', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'slim-fit-jeans'), '/assets/images/products/slim-jeans.svg', 'Slim Fit Jeans', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'everyday-cotton-tshirt'), '/assets/images/products/basic-tshirt.svg', 'Everyday Cotton T-Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'kids-comfort-hoodie'), '/assets/images/products/kids-hoodie.svg', 'Kids Comfort Hoodie', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'soft-home-cushion'), '/assets/images/products/home-cushion.svg', 'Soft Home Cushion', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'hydrating-beauty-serum'), '/assets/images/products/beauty-serum.svg', 'Hydrating Beauty Serum', TRUE, 1),
((SELECT product_id FROM products WHERE slug = 'urban-travel-backpack'), '/assets/images/products/travel-backpack.svg', 'Urban Travel Backpack', TRUE, 1);

SET SQL_SAFE_UPDATES = 1;