USE myntrademo_db;

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
('Roadster', 'roadster', 'Casual everyday fashion brand', NULL, TRUE),
('HIGHLANDER', 'highlander', 'Smart casual menswear brand', NULL, TRUE),
('Tokyo Talkies', 'tokyo-talkies', 'Trendy women fashion brand', NULL, TRUE),
('Anouk', 'anouk', 'Indian ethnic wear brand', NULL, TRUE),
('HRX', 'hrx', 'Activewear and sports lifestyle brand', NULL, TRUE),
('Puma', 'puma', 'Sportswear and footwear brand', NULL, TRUE),
('DressBerry', 'dressberry', 'Modern women fashion brand', NULL, TRUE),
('Mast & Harbour', 'mast-harbour', 'Casual lifestyle fashion brand', NULL, TRUE),
('Kook N Keech', 'kook-n-keech', 'Youth streetwear brand', NULL, TRUE),
('House of Pataudi', 'house-of-pataudi', 'Premium ethnic fashion brand', NULL, TRUE),
('Home Centre', 'home-centre', 'Home decor and furnishing brand', NULL, TRUE),
('Lakme', 'lakme', 'Beauty and cosmetics brand', NULL, TRUE)
ON DUPLICATE KEY UPDATE
brand_name = VALUES(brand_name),
description = VALUES(description),
is_active = VALUES(is_active);

INSERT INTO products
(category_id, brand_id, product_name, slug, short_description, long_description, base_price, selling_price, discount_percent, product_status, is_featured)
VALUES
((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='roadster'),
'Men Solid Casual Shirt', 'men-solid-casual-shirt', 'Men solid casual shirt with regular fit.',
'Comfortable casual shirt for everyday styling with jeans or chinos.', 1999.00, 899.00, 55.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='highlander'),
'Men Slim Fit Chinos', 'men-slim-fit-chinos', 'Slim fit casual chinos.',
'Smart slim fit chinos suitable for office, college, and weekend wear.', 2499.00, 1199.00, 52.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='mast-harbour'),
'Men Printed Polo T-Shirt', 'men-printed-polo-tshirt', 'Printed polo neck t-shirt.',
'Soft cotton blend polo t-shirt with a clean printed design.', 1499.00, 699.00, 53.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='kook-n-keech'),
'Men Oversized Graphic T-Shirt', 'men-oversized-graphic-tshirt', 'Oversized streetwear t-shirt.',
'Relaxed oversized graphic t-shirt for streetwear-inspired outfits.', 1299.00, 599.00, 54.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='roadster'),
'Men Black Bomber Jacket', 'men-black-bomber-jacket', 'Black bomber jacket with zip closure.',
'Layer-ready bomber jacket with clean finish and comfortable fit.', 3999.00, 1999.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='tokyo-talkies'),
'Women Floral A-Line Dress', 'women-floral-aline-dress', 'Floral printed A-line dress.',
'Easygoing floral dress designed for casual outings and day wear.', 2299.00, 999.00, 57.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='dressberry'),
'Women High-Rise Skinny Jeans', 'women-high-rise-skinny-jeans', 'High-rise skinny fit jeans.',
'Stretchable skinny jeans with a clean high-rise fit.', 2599.00, 1299.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='anouk'),
'Women Embroidered Kurta', 'women-embroidered-kurta', 'Elegant embroidered straight kurta.',
'Comfortable ethnic kurta with elegant embroidery and daily wear fit.', 2499.00, 1249.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='house-of-pataudi'),
'Women Ethnic Printed Palazzo', 'women-ethnic-printed-palazzo', 'Printed ethnic palazzo pants.',
'Lightweight palazzo pants for ethnic and fusion styling.', 1799.00, 799.00, 56.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='dressberry'),
'Women Solid Crop Top', 'women-solid-crop-top', 'Solid casual crop top.',
'Minimal crop top for casual everyday styling.', 999.00, 449.00, 55.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='footwear'), (SELECT brand_id FROM brands WHERE slug='puma'),
'Unisex Running Shoes', 'unisex-running-shoes', 'Lightweight running shoes.',
'Performance-inspired running shoes with cushioned sole and breathable upper.', 4999.00, 2499.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='footwear'), (SELECT brand_id FROM brands WHERE slug='hrx'),
'Men Training Sneakers', 'men-training-sneakers', 'Training sneakers for active use.',
'Comfortable sneakers for training, walking, and daily casual wear.', 3499.00, 1599.00, 54.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='footwear'), (SELECT brand_id FROM brands WHERE slug='puma'),
'Women White Casual Sneakers', 'women-white-casual-sneakers', 'White casual sneakers.',
'Clean white sneakers suitable for dresses, denim, and daily outfits.', 3999.00, 1899.00, 53.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='footwear'), (SELECT brand_id FROM brands WHERE slug='hrx'),
'Men Sports Sandals', 'men-sports-sandals', 'Comfort sports sandals.',
'Lightweight sandals with comfortable sole for casual outdoor use.', 1999.00, 899.00, 55.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='kids'), (SELECT brand_id FROM brands WHERE slug='kook-n-keech'),
'Boys Printed T-Shirt', 'boys-printed-tshirt', 'Printed cotton t-shirt for boys.',
'Soft cotton printed t-shirt designed for daily kids wear.', 799.00, 399.00, 50.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='kids'), (SELECT brand_id FROM brands WHERE slug='roadster'),
'Girls Denim Dungaree', 'girls-denim-dungaree', 'Denim dungaree for girls.',
'Comfortable denim dungaree with casual styling for kids.', 1899.00, 999.00, 47.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='kids'), (SELECT brand_id FROM brands WHERE slug='hrx'),
'Kids Joggers Pack', 'kids-joggers-pack', 'Comfort joggers for kids.',
'Soft joggers suitable for playtime, travel, and casual wear.', 1599.00, 799.00, 50.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='accessories'), (SELECT brand_id FROM brands WHERE slug='mast-harbour'),
'Men Leather Wallet', 'men-leather-wallet', 'Compact leather wallet.',
'Classic compact wallet with organized card and cash sections.', 1299.00, 599.00, 54.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='accessories'), (SELECT brand_id FROM brands WHERE slug='dressberry'),
'Women Sling Bag', 'women-sling-bag', 'Compact sling bag.',
'Stylish sling bag with everyday storage and clean finish.', 2299.00, 1099.00, 52.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='accessories'), (SELECT brand_id FROM brands WHERE slug='roadster'),
'Unisex Baseball Cap', 'unisex-baseball-cap', 'Classic baseball cap.',
'Adjustable cap for casual outfits and sun protection.', 799.00, 349.00, 56.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='accessories'), (SELECT brand_id FROM brands WHERE slug='mast-harbour'),
'Men Analog Watch', 'men-analog-watch', 'Analog wrist watch.',
'Minimal analog watch with everyday premium look.', 2999.00, 1499.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='home'), (SELECT brand_id FROM brands WHERE slug='home-centre'),
'Bedsheet Set With Pillow Covers', 'bedsheet-set-pillow-covers', 'Soft bedsheet set.',
'Comfortable bedsheet set with matching pillow covers for bedroom styling.', 2499.00, 1199.00, 52.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='home'), (SELECT brand_id FROM brands WHERE slug='home-centre'),
'Decorative Table Lamp', 'decorative-table-lamp', 'Modern decorative table lamp.',
'Warm table lamp designed for bedroom and living room ambience.', 3499.00, 1699.00, 51.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='home'), (SELECT brand_id FROM brands WHERE slug='home-centre'),
'Cotton Bath Towels Set', 'cotton-bath-towels-set', 'Soft cotton towel set.',
'Absorbent towel set made for daily bathroom use.', 1499.00, 699.00, 53.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='beauty'), (SELECT brand_id FROM brands WHERE slug='lakme'),
'Matte Liquid Lipstick', 'matte-liquid-lipstick', 'Long stay matte lipstick.',
'Smooth matte lipstick with rich color payoff and comfortable finish.', 799.00, 399.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='beauty'), (SELECT brand_id FROM brands WHERE slug='lakme'),
'Face Moisturizer Cream', 'face-moisturizer-cream', 'Daily face moisturizer.',
'Lightweight moisturizer for soft and hydrated skin.', 599.00, 299.00, 50.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='beauty'), (SELECT brand_id FROM brands WHERE slug='lakme'),
'Compact Powder', 'compact-powder', 'Oil control compact powder.',
'Everyday compact powder with smooth finish and oil control.', 699.00, 349.00, 50.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='tokyo-talkies'),
'Women Denim Jacket', 'women-denim-jacket', 'Casual denim jacket.',
'Classic denim jacket for layering over tops, dresses, and tees.', 2999.00, 1499.00, 50.00, 'ACTIVE', TRUE),

((SELECT category_id FROM categories WHERE slug='men'), (SELECT brand_id FROM brands WHERE slug='highlander'),
'Men Checked Casual Shirt', 'men-checked-casual-shirt', 'Checked casual shirt.',
'Soft checked shirt with regular fit for everyday casual wear.', 1799.00, 799.00, 56.00, 'ACTIVE', FALSE),

((SELECT category_id FROM categories WHERE slug='women'), (SELECT brand_id FROM brands WHERE slug='anouk'),
'Women Printed Saree', 'women-printed-saree', 'Printed ethnic saree.',
'Elegant printed saree suitable for festive and casual ethnic styling.', 3499.00, 1599.00, 54.00, 'ACTIVE', TRUE)
ON DUPLICATE KEY UPDATE
product_name = VALUES(product_name),
short_description = VALUES(short_description),
long_description = VALUES(long_description),
base_price = VALUES(base_price),
selling_price = VALUES(selling_price),
discount_percent = VALUES(discount_percent),
product_status = VALUES(product_status),
is_featured = VALUES(is_featured);

DELETE pi
FROM product_images pi
INNER JOIN products p ON pi.product_id = p.product_id
WHERE p.slug IN (
'men-solid-casual-shirt','men-slim-fit-chinos','men-printed-polo-tshirt','men-oversized-graphic-tshirt','men-black-bomber-jacket',
'women-floral-aline-dress','women-high-rise-skinny-jeans','women-embroidered-kurta','women-ethnic-printed-palazzo','women-solid-crop-top',
'unisex-running-shoes','men-training-sneakers','women-white-casual-sneakers','men-sports-sandals',
'boys-printed-tshirt','girls-denim-dungaree','kids-joggers-pack',
'men-leather-wallet','women-sling-bag','unisex-baseball-cap','men-analog-watch',
'bedsheet-set-pillow-covers','decorative-table-lamp','cotton-bath-towels-set',
'matte-liquid-lipstick','face-moisturizer-cream','compact-powder',
'women-denim-jacket','men-checked-casual-shirt','women-printed-saree'
);

INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order)
VALUES
((SELECT product_id FROM products WHERE slug='men-solid-casual-shirt'), 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?auto=format&fit=crop&w=900&q=80', 'Men Solid Casual Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-slim-fit-chinos'), 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=900&q=80', 'Men Slim Fit Chinos', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-printed-polo-tshirt'), 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?auto=format&fit=crop&w=900&q=80', 'Men Printed Polo T-Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-oversized-graphic-tshirt'), 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=80', 'Men Oversized Graphic T-Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-black-bomber-jacket'), 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=900&q=80', 'Men Black Bomber Jacket', TRUE, 1),

((SELECT product_id FROM products WHERE slug='women-floral-aline-dress'), 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?auto=format&fit=crop&w=900&q=80', 'Women Floral A-Line Dress', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-high-rise-skinny-jeans'), 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=900&q=80', 'Women High-Rise Skinny Jeans', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-embroidered-kurta'), 'https://images.unsplash.com/photo-1594633313593-bab3825d0caf?auto=format&fit=crop&w=900&q=80', 'Women Embroidered Kurta', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-ethnic-printed-palazzo'), 'https://images.unsplash.com/photo-1578632292335-df3abbb0d586?auto=format&fit=crop&w=900&q=80', 'Women Ethnic Printed Palazzo', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-solid-crop-top'), 'https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?auto=format&fit=crop&w=900&q=80', 'Women Solid Crop Top', TRUE, 1),

((SELECT product_id FROM products WHERE slug='unisex-running-shoes'), 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=80', 'Unisex Running Shoes', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-training-sneakers'), 'https://images.unsplash.com/photo-1549298916-b41d501d3772?auto=format&fit=crop&w=900&q=80', 'Men Training Sneakers', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-white-casual-sneakers'), 'https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?auto=format&fit=crop&w=900&q=80', 'Women White Casual Sneakers', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-sports-sandals'), 'https://images.unsplash.com/photo-1562273138-f46be4ebdf33?auto=format&fit=crop&w=900&q=80', 'Men Sports Sandals', TRUE, 1),

((SELECT product_id FROM products WHERE slug='boys-printed-tshirt'), 'https://images.unsplash.com/photo-1519238263530-99bdd11df2ea?auto=format&fit=crop&w=900&q=80', 'Boys Printed T-Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug='girls-denim-dungaree'), 'https://images.unsplash.com/photo-1503919545889-aef636e10ad4?auto=format&fit=crop&w=900&q=80', 'Girls Denim Dungaree', TRUE, 1),
((SELECT product_id FROM products WHERE slug='kids-joggers-pack'), 'https://images.unsplash.com/photo-1519457431-44ccd64a579b?auto=format&fit=crop&w=900&q=80', 'Kids Joggers Pack', TRUE, 1),

((SELECT product_id FROM products WHERE slug='men-leather-wallet'), 'https://images.unsplash.com/photo-1627123424574-724758594e93?auto=format&fit=crop&w=900&q=80', 'Men Leather Wallet', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-sling-bag'), 'https://images.unsplash.com/photo-1594223274512-ad4803739b7c?auto=format&fit=crop&w=900&q=80', 'Women Sling Bag', TRUE, 1),
((SELECT product_id FROM products WHERE slug='unisex-baseball-cap'), 'https://images.unsplash.com/photo-1588850561407-ed78c282e89b?auto=format&fit=crop&w=900&q=80', 'Unisex Baseball Cap', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-analog-watch'), 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=900&q=80', 'Men Analog Watch', TRUE, 1),

((SELECT product_id FROM products WHERE slug='bedsheet-set-pillow-covers'), 'https://images.unsplash.com/photo-1616594039964-ae9021a400a0?auto=format&fit=crop&w=900&q=80', 'Bedsheet Set With Pillow Covers', TRUE, 1),
((SELECT product_id FROM products WHERE slug='decorative-table-lamp'), 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=900&q=80', 'Decorative Table Lamp', TRUE, 1),
((SELECT product_id FROM products WHERE slug='cotton-bath-towels-set'), 'https://images.unsplash.com/photo-1631889993959-41b4e9c6e3c5?auto=format&fit=crop&w=900&q=80', 'Cotton Bath Towels Set', TRUE, 1),

((SELECT product_id FROM products WHERE slug='matte-liquid-lipstick'), 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=900&q=80', 'Matte Liquid Lipstick', TRUE, 1),
((SELECT product_id FROM products WHERE slug='face-moisturizer-cream'), 'https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=900&q=80', 'Face Moisturizer Cream', TRUE, 1),
((SELECT product_id FROM products WHERE slug='compact-powder'), 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?auto=format&fit=crop&w=900&q=80', 'Compact Powder', TRUE, 1),

((SELECT product_id FROM products WHERE slug='women-denim-jacket'), 'https://images.unsplash.com/photo-1543076447-215ad9ba6923?auto=format&fit=crop&w=900&q=80', 'Women Denim Jacket', TRUE, 1),
((SELECT product_id FROM products WHERE slug='men-checked-casual-shirt'), 'https://images.unsplash.com/photo-1598033129183-c4f50c736f10?auto=format&fit=crop&w=900&q=80', 'Men Checked Casual Shirt', TRUE, 1),
((SELECT product_id FROM products WHERE slug='women-printed-saree'), 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=900&q=80', 'Women Printed Saree', TRUE, 1);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-S'), 'S', 'Default', base_price, selling_price, 35, 5, TRUE FROM products
WHERE slug IN (
'men-solid-casual-shirt','men-slim-fit-chinos','men-printed-polo-tshirt','men-oversized-graphic-tshirt','men-black-bomber-jacket',
'women-floral-aline-dress','women-high-rise-skinny-jeans','women-embroidered-kurta','women-ethnic-printed-palazzo','women-solid-crop-top',
'boys-printed-tshirt','girls-denim-dungaree','kids-joggers-pack','women-denim-jacket','men-checked-casual-shirt','women-printed-saree'
)
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-M'), 'M', 'Default', base_price, selling_price, 45, 5, TRUE FROM products
WHERE slug IN (
'men-solid-casual-shirt','men-slim-fit-chinos','men-printed-polo-tshirt','men-oversized-graphic-tshirt','men-black-bomber-jacket',
'women-floral-aline-dress','women-high-rise-skinny-jeans','women-embroidered-kurta','women-ethnic-printed-palazzo','women-solid-crop-top',
'boys-printed-tshirt','girls-denim-dungaree','kids-joggers-pack','women-denim-jacket','men-checked-casual-shirt','women-printed-saree'
)
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-L'), 'L', 'Default', base_price, selling_price, 40, 5, TRUE FROM products
WHERE slug IN (
'men-solid-casual-shirt','men-slim-fit-chinos','men-printed-polo-tshirt','men-oversized-graphic-tshirt','men-black-bomber-jacket',
'women-floral-aline-dress','women-high-rise-skinny-jeans','women-embroidered-kurta','women-ethnic-printed-palazzo','women-solid-crop-top',
'boys-printed-tshirt','girls-denim-dungaree','kids-joggers-pack','women-denim-jacket','men-checked-casual-shirt','women-printed-saree'
)
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-STD'), 'Free Size', 'Default', base_price, selling_price, 50, 5, TRUE FROM products
WHERE slug IN (
'men-leather-wallet','women-sling-bag','unisex-baseball-cap','men-analog-watch',
'bedsheet-set-pillow-covers','decorative-table-lamp','cotton-bath-towels-set',
'matte-liquid-lipstick','face-moisturizer-cream','compact-powder'
)
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-UK7'), 'UK 7', 'Default', base_price, selling_price, 25, 5, TRUE FROM products
WHERE slug IN ('unisex-running-shoes','men-training-sneakers','women-white-casual-sneakers','men-sports-sandals')
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-UK8'), 'UK 8', 'Default', base_price, selling_price, 30, 5, TRUE FROM products
WHERE slug IN ('unisex-running-shoes','men-training-sneakers','women-white-casual-sneakers','men-sports-sandals')
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);

INSERT INTO product_variants (product_id, sku, size, color, price, selling_price, stock_quantity, low_stock_limit, is_active)
SELECT product_id, CONCAT('MD-', slug, '-UK9'), 'UK 9', 'Default', base_price, selling_price, 22, 5, TRUE FROM products
WHERE slug IN ('unisex-running-shoes','men-training-sneakers','women-white-casual-sneakers','men-sports-sandals')
ON DUPLICATE KEY UPDATE
price = VALUES(price),
selling_price = VALUES(selling_price),
stock_quantity = VALUES(stock_quantity),
is_active = VALUES(is_active);