USE myntrademo_db;

CREATE TABLE IF NOT EXISTS checkout_payment_methods (
    method_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    method_code VARCHAR(50) NOT NULL UNIQUE,
    method_label VARCHAR(120) NOT NULL,
    detail_title VARCHAR(160) NOT NULL,
    detail_description VARCHAR(255) NULL,
    tab_offer_text VARCHAR(80) NULL,
    fee_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    recommended BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS checkout_payment_options (
    option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    method_code VARCHAR(50) NOT NULL,
    option_code VARCHAR(80) NOT NULL,
    option_label VARCHAR(160) NOT NULL,
    option_subtitle VARCHAR(255) NULL,
    option_notice VARCHAR(255) NULL,
    logo_text VARCHAR(20) NULL,
    min_order_amount DECIMAL(10,2) NULL,
    disabled BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_payment_option_code (method_code, option_code),
    INDEX idx_payment_option_method (method_code, active, sort_order)
);

INSERT INTO checkout_payment_methods
(method_code, method_label, detail_title, detail_description, tab_offer_text, fee_amount, recommended, active, sort_order)
VALUES
('CASH_ON_DELIVERY', 'Cash On Delivery', 'Cash on Delivery (Cash/UPI)', 'For this option, there is a fee of ₹10. You can pay online to avoid this.', NULL, 10.00, TRUE, TRUE, 10),
('UPI', 'UPI (Pay via any App)', 'Pay using UPI', 'Use any UPI app to complete payment instantly and securely.', NULL, 0.00, FALSE, TRUE, 20),
('CARD', 'Credit/Debit Card', 'CREDIT/ DEBIT CARD', 'Please ensure your card can be used for online transactions.', '11 Offers', 0.00, FALSE, TRUE, 30),
('WALLET', 'Wallets', 'Select wallet to pay', 'Choose an eligible wallet to pay securely.', NULL, 0.00, FALSE, TRUE, 40),
('PAY_LATER', 'Pay Later', 'Select Pay Later to pay', 'Use eligible pay later options and complete your order now.', NULL, 0.00, FALSE, TRUE, 50),
('EMI', 'EMI', 'Select EMI Option', 'Convert eligible card payments into monthly instalments.', '1 Offer', 0.00, FALSE, TRUE, 60),
('NET_BANKING', 'Net Banking', 'Net Banking', 'Choose your bank and complete payment securely.', NULL, 0.00, FALSE, TRUE, 70)
ON DUPLICATE KEY UPDATE
method_label = VALUES(method_label),
detail_title = VALUES(detail_title),
detail_description = VALUES(detail_description),
tab_offer_text = VALUES(tab_offer_text),
fee_amount = VALUES(fee_amount),
recommended = VALUES(recommended),
active = VALUES(active),
sort_order = VALUES(sort_order);

INSERT INTO checkout_payment_options
(method_code, option_code, option_label, option_subtitle, option_notice, logo_text, min_order_amount, disabled, active, sort_order)
VALUES
('CASH_ON_DELIVERY', 'COD_CASH_UPI', 'Cash on Delivery (Cash/UPI)', 'Pay when the order is delivered.', 'For this option, there is a fee of ₹10. You can pay online to avoid this.', '₹', NULL, FALSE, TRUE, 10),

('UPI', 'UPI_SCAN_PAY', 'Scan & Pay', 'Scan QR using any UPI app.', NULL, 'QR', NULL, FALSE, TRUE, 10),
('UPI', 'UPI_ID', 'Enter UPI ID', 'Pay using your UPI ID.', NULL, 'UPI', NULL, FALSE, TRUE, 20),

('WALLET', 'MOBIKWIK', 'Mobikwik', 'Link Account', 'Mobikwik is currently facing low success rate.', 'M', NULL, FALSE, TRUE, 10),
('WALLET', 'AIRTEL_PAYMENTS_BANK', 'Airtel Payments Bank', NULL, 'Airtel Payments Bank is currently facing high failure rate.', 'A', NULL, TRUE, TRUE, 20),
('WALLET', 'PAYZAPP', 'PayZapp', NULL, NULL, 'PZ', NULL, FALSE, TRUE, 30),
('WALLET', 'OLAMONEY', 'OlaMoney (Wallet + Postpaid)', NULL, NULL, 'OM', NULL, FALSE, TRUE, 40),

('PAY_LATER', 'LAZYPAY', 'Lazypay', NULL, NULL, 'LP', NULL, FALSE, TRUE, 10),

('EMI', 'HDFC_EMI', 'HDFC Credit Card EMI', NULL, 'Available on min. order of ₹3000', 'HDFC', 3000.00, FALSE, TRUE, 10),
('EMI', 'RBL_EMI', 'RBL Bank Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'RBL', 2500.00, FALSE, TRUE, 20),
('EMI', 'KOTAK_EMI', 'Kotak Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'KOTAK', 2500.00, FALSE, TRUE, 30),
('EMI', 'SBI_EMI', 'SBI Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'SBI', 2500.00, FALSE, TRUE, 40),
('EMI', 'AXIS_EMI', 'Axis Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'AXIS', 2500.00, FALSE, TRUE, 50),
('EMI', 'BOBCARD_EMI', 'BOBCARD EMI', NULL, 'Available on min. order of ₹2500', 'BOB', 2500.00, FALSE, TRUE, 60),
('EMI', 'ICICI_EMI', 'ICICI Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'ICICI', 2500.00, FALSE, TRUE, 70),
('EMI', 'CANARA_EMI', 'Canara Bank Credit Card EMI', NULL, 'Available on min. order of ₹2500', 'CAN', 2500.00, FALSE, TRUE, 80),

('NET_BANKING', 'AXIS_BANK', 'Axis Bank', NULL, 'Axis Bank is currently facing low success rate.', 'AXIS', NULL, FALSE, TRUE, 10),
('NET_BANKING', 'HDFC_BANK', 'HDFC Bank', NULL, NULL, 'HDFC', NULL, FALSE, TRUE, 20),
('NET_BANKING', 'ICICI_BANK', 'ICICI Bank', NULL, NULL, 'ICICI', NULL, FALSE, TRUE, 30),
('NET_BANKING', 'KOTAK_BANK', 'Kotak', NULL, NULL, 'KOTAK', NULL, FALSE, TRUE, 40),
('NET_BANKING', 'SBI_BANK', 'SBI', NULL, 'SBI is currently facing high failure rate.', 'SBI', NULL, TRUE, TRUE, 50),
('NET_BANKING', 'AU_SMALL_FINANCE_BANK', 'AU Small Finance Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 60),
('NET_BANKING', 'BANDHAN_BANK', 'Bandhan Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 70),
('NET_BANKING', 'BANK_OF_BARODA_CORPORATE', 'Bank of Baroda Corporate', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 80),
('NET_BANKING', 'BANK_OF_BARODA_RETAIL', 'Bank of Baroda Retail Accounts', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 90),
('NET_BANKING', 'BANK_OF_INDIA', 'Bank of India', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 100),
('NET_BANKING', 'BANK_OF_MAHARASHTRA', 'Bank of Maharashtra', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 110),
('NET_BANKING', 'CANARA_BANK', 'Canara Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 120),
('NET_BANKING', 'CATHOLIC_SYRIAN_BANK', 'Catholic Syrian Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 130),
('NET_BANKING', 'CENTRAL_BANK_OF_INDIA', 'Central Bank of India', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 140),
('NET_BANKING', 'CITY_UNION_BANK', 'City Union Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 150),
('NET_BANKING', 'DCB_BANK', 'DCB Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 160),
('NET_BANKING', 'FEDERAL_BANK', 'Federal Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 170),
('NET_BANKING', 'IDBI_BANK', 'IDBI Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 180),
('NET_BANKING', 'IDFC_FIRST_BANK', 'IDFC First Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 190),
('NET_BANKING', 'INDIAN_BANK', 'Indian Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 200),
('NET_BANKING', 'INDIAN_OVERSEAS_BANK', 'Indian Overseas Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 210),
('NET_BANKING', 'INDUSIND_BANK', 'IndusInd Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 220),
('NET_BANKING', 'KARNATAKA_BANK', 'Karnataka Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 230),
('NET_BANKING', 'KARUR_VYSYA_BANK', 'Karur Vysya Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 240),
('NET_BANKING', 'PUNJAB_NATIONAL_BANK', 'Punjab National Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 250),
('NET_BANKING', 'SOUTH_INDIAN_BANK', 'South Indian Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 260),
('NET_BANKING', 'TAMILNAD_MERCANTILE_BANK', 'Tamilnad Mercantile Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 270),
('NET_BANKING', 'UCO_BANK', 'UCO Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 280),
('NET_BANKING', 'UNION_BANK_OF_INDIA', 'Union Bank of India', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 290),
('NET_BANKING', 'YES_BANK', 'YES Bank', NULL, NULL, 'BANK', NULL, FALSE, TRUE, 300)
ON DUPLICATE KEY UPDATE
option_label = VALUES(option_label),
option_subtitle = VALUES(option_subtitle),
option_notice = VALUES(option_notice),
logo_text = VALUES(logo_text),
min_order_amount = VALUES(min_order_amount),
disabled = VALUES(disabled),
active = VALUES(active),
sort_order = VALUES(sort_order);