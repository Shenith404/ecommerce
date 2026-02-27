-- ==============================
-- E-Commerce Computer Shop - Seed Data
-- Single Store with Computer Products
-- ==============================

-- ==============================
-- 1. SELLER (Store Owner)
-- ==============================
INSERT INTO seller (id, seller_name, mobile, email, password, gstin, role, is_email_verified, account_status,
                   business_name, business_email, business_mobile, business_address,
                   account_holder_name, account_number, ifsc_code,
                   created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'John Smith',
    '+1-555-0100',
    'john.smith@techstore.com',
    '$2a$10$YQlZvZ7Z7Z7Z7Z7Z7Z7Z7Z',  -- hashed password (password123)
    'GST123456789',
    'ROLE_SELLER',
    true,
    'ACTIVE',
    'Tech Haven Store',
    'business@techstore.com',
    '+1-555-0101',
    '123 Tech Street, Silicon Valley, CA 94025, USA',
    'John Smith',
    '1234567890',
    'TECH0001234',
    NOW(),
    NOW()
);

-- ==============================
-- 2. ADDRESS (Pickup Address)
-- ==============================
INSERT INTO address (id, address, city, state, postal_code, country, mobile, seller_id, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    '123 Tech Street, Warehouse A',
    'Silicon Valley',
    'California',
    '94025',
    'USA',
    '+1-555-0101',
    '550e8400-e29b-41d4-a716-446655440001',
    NOW(),
    NOW()
);

-- ==============================
-- 3. STORE
-- ==============================
INSERT INTO stores (id, owner_id, store_name, seo_slug, description, average_rating, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440001',
    'Tech Haven',
    'tech-haven',
    'Your one-stop shop for computers, laptops, and tech accessories. We offer the latest technology at competitive prices with exceptional customer service.',
    4.7,
    NOW(),
    NOW()
);

-- ==============================
-- 4. BRANDS
-- ==============================
INSERT INTO brands (id, name, seo_slug, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'Dell', 'dell', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440011', 'HP', 'hp', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440012', 'Lenovo', 'lenovo', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440013', 'ASUS', 'asus', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440014', 'Apple', 'apple', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440015', 'Acer', 'acer', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440016', 'MSI', 'msi', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440017', 'Samsung', 'samsung', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440018', 'LG', 'lg', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440019', 'Logitech', 'logitech', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440020', 'Razer', 'razer', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440021', 'Corsair', 'corsair', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440022', 'Intel', 'intel', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440023', 'AMD', 'amd', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440024', 'NVIDIA', 'nvidia', NOW(), NOW());

-- ==============================
-- 5. CATEGORIES (Hierarchical)
-- ==============================

-- Level 1: Root Categories
INSERT INTO category (id, name, slug, level, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440030', 'Computers', 'computers', 1, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440031', 'Accessories', 'accessories', 1, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440032', 'Components', 'components', 1, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440033', 'Monitors', 'monitors', 1, NOW(), NOW());

-- Level 2: Sub-categories under Computers
INSERT INTO category (id, name, slug, level, parent_id, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440040', 'Laptops', 'laptops', 2, '550e8400-e29b-41d4-a716-446655440030', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440041', 'Desktops', 'desktops', 2, '550e8400-e29b-41d4-a716-446655440030', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440042', 'Tablets', 'tablets', 2, '550e8400-e29b-41d4-a716-446655440030', NOW(), NOW());

-- Level 2: Sub-categories under Accessories
INSERT INTO category (id, name, slug, level, parent_id, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440050', 'Keyboards', 'keyboards', 2, '550e8400-e29b-41d4-a716-446655440031', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440051', 'Mice', 'mice', 2, '550e8400-e29b-41d4-a716-446655440031', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440052', 'Headsets', 'headsets', 2, '550e8400-e29b-41d4-a716-446655440031', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440053', 'Webcams', 'webcams', 2, '550e8400-e29b-41d4-a716-446655440031', NOW(), NOW());

-- Level 2: Sub-categories under Components
INSERT INTO category (id, name, slug, level, parent_id, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440060', 'Processors', 'processors', 2, '550e8400-e29b-41d4-a716-446655440032', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440061', 'Graphics Cards', 'graphics-cards', 2, '550e8400-e29b-41d4-a716-446655440032', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440062', 'RAM', 'ram', 2, '550e8400-e29b-41d4-a716-446655440032', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440063', 'Storage', 'storage', 2, '550e8400-e29b-41d4-a716-446655440032', NOW(), NOW());

-- ==============================
-- 6. CATEGORY-BRAND RELATIONSHIPS
-- ==============================
INSERT INTO category_brands (category_id, brand_id) VALUES
-- Laptops
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440010'), -- Dell
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440011'), -- HP
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440012'), -- Lenovo
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440013'), -- ASUS
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440014'), -- Apple
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440015'), -- Acer
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440016'), -- MSI

-- Desktops
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440010'), -- Dell
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440011'), -- HP
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440012'), -- Lenovo
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440014'), -- Apple

-- Monitors
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440010'), -- Dell
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440013'), -- ASUS
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440017'), -- Samsung
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440018'), -- LG

-- Keyboards & Mice
('550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440019'), -- Logitech
('550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440020'), -- Razer
('550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440021'), -- Corsair
('550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440019'), -- Logitech
('550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440020'), -- Razer

-- Headsets
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440019'), -- Logitech
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440020'), -- Razer
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440021'), -- Corsair

-- Processors
('550e8400-e29b-41d4-a716-446655440060', '550e8400-e29b-41d4-a716-446655440022'), -- Intel
('550e8400-e29b-41d4-a716-446655440060', '550e8400-e29b-41d4-a716-446655440023'), -- AMD

-- Graphics Cards
('550e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440024'), -- NVIDIA
('550e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440023'), -- AMD
('550e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440013'); -- ASUS

-- ==============================
-- 7. SPECIFICATION KEYS
-- ==============================
INSERT INTO specification_keys (id, name, is_required, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440070', 'Processor', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440071', 'RAM', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440072', 'Storage', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440073', 'Screen Size', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440074', 'Graphics Card', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440075', 'Operating System', true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440076', 'Resolution', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440077', 'Refresh Rate', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440078', 'Connectivity', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440079', 'Color', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440080', 'DPI', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440081', 'Switch Type', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440082', 'Memory Type', false, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440083', 'Interface', false, NOW(), NOW());

-- ==============================
-- 8. SPECIFICATION OPTIONS
-- ==============================

-- Processor options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440100', '550e8400-e29b-41d4-a716-446655440070', 'Intel Core i5-12400', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440101', '550e8400-e29b-41d4-a716-446655440070', 'Intel Core i7-12700K', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440102', '550e8400-e29b-41d4-a716-446655440070', 'Intel Core i9-13900K', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440103', '550e8400-e29b-41d4-a716-446655440070', 'AMD Ryzen 5 5600X', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440104', '550e8400-e29b-41d4-a716-446655440070', 'AMD Ryzen 7 5800X', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440105', '550e8400-e29b-41d4-a716-446655440070', 'AMD Ryzen 9 5950X', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440106', '550e8400-e29b-41d4-a716-446655440070', 'Apple M2', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440107', '550e8400-e29b-41d4-a716-446655440070', 'Apple M2 Pro', NOW(), NOW());

-- RAM options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440110', '550e8400-e29b-41d4-a716-446655440071', '8GB', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440111', '550e8400-e29b-41d4-a716-446655440071', '16GB', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440112', '550e8400-e29b-41d4-a716-446655440071', '32GB', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440113', '550e8400-e29b-41d4-a716-446655440071', '64GB', NOW(), NOW());

-- Storage options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440120', '550e8400-e29b-41d4-a716-446655440072', '256GB SSD', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440121', '550e8400-e29b-41d4-a716-446655440072', '512GB SSD', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440122', '550e8400-e29b-41d4-a716-446655440072', '1TB SSD', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440123', '550e8400-e29b-41d4-a716-446655440072', '2TB SSD', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440124', '550e8400-e29b-41d4-a716-446655440072', '1TB HDD', NOW(), NOW());

-- Screen Size options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440130', '550e8400-e29b-41d4-a716-446655440073', '13.3 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440131', '550e8400-e29b-41d4-a716-446655440073', '14 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440132', '550e8400-e29b-41d4-a716-446655440073', '15.6 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440133', '550e8400-e29b-41d4-a716-446655440073', '17.3 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440134', '550e8400-e29b-41d4-a716-446655440073', '24 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440135', '550e8400-e29b-41d4-a716-446655440073', '27 inch', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440136', '550e8400-e29b-41d4-a716-446655440073', '32 inch', NOW(), NOW());

-- Graphics Card options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440140', '550e8400-e29b-41d4-a716-446655440074', 'NVIDIA RTX 3060', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440141', '550e8400-e29b-41d4-a716-446655440074', 'NVIDIA RTX 3070', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440142', '550e8400-e29b-41d4-a716-446655440074', 'NVIDIA RTX 4080', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440143', '550e8400-e29b-41d4-a716-446655440074', 'NVIDIA RTX 4090', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440144', '550e8400-e29b-41d4-a716-446655440074', 'AMD RX 6800 XT', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440145', '550e8400-e29b-41d4-a716-446655440074', 'Integrated Graphics', NOW(), NOW());

-- Operating System options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440150', '550e8400-e29b-41d4-a716-446655440075', 'Windows 11 Home', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440151', '550e8400-e29b-41d4-a716-446655440075', 'Windows 11 Pro', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440152', '550e8400-e29b-41d4-a716-446655440075', 'macOS Ventura', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440153', '550e8400-e29b-41d4-a716-446655440075', 'Ubuntu Linux', NOW(), NOW());

-- Resolution options
INSERT INTO specification_options (id, spec_key_id, value, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440160', '550e8400-e29b-41d4-a716-446655440076', '1920x1080 (Full HD)', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440161', '550e8400-e29b-41d4-a716-446655440076', '2560x1440 (QHD)', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440162', '550e8400-e29b-41d4-a716-446655440076', '3840x2160 (4K)', NOW(), NOW());

-- ==============================
-- 9. CATEGORY-SPECIFICATION RELATIONSHIPS
-- ==============================
INSERT INTO category_specifications (category_id, spec_key_id) VALUES
-- Laptops
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440070'), -- Processor
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440071'), -- RAM
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440072'), -- Storage
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440073'), -- Screen Size
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440074'), -- Graphics Card
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440075'), -- Operating System

-- Desktops
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440070'), -- Processor
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440071'), -- RAM
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440072'), -- Storage
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440074'), -- Graphics Card
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440075'), -- Operating System

-- Monitors
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440073'), -- Screen Size
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440076'), -- Resolution
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440077'); -- Refresh Rate

-- ==============================
-- 10. PRODUCTS
-- ==============================

-- LAPTOPS
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440200',
    'Dell XPS 15 9530 Laptop',
    'dell-xps-15-9530',
    'Premium ultrabook with stunning InfinityEdge display. Perfect for creative professionals and power users. Features 12th Gen Intel processors and stunning 3.5K OLED display.',
    'NEW',
    '{"Processor": "Intel Core i7-12700K", "RAM": "16GB", "Storage": "512GB SSD", "Screen Size": "15.6 inch", "Graphics Card": "NVIDIA RTX 3060", "Operating System": "Windows 11 Pro", "Display": "3.5K OLED", "Weight": "2.0 kg"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440010',
    '550e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440001',
    45,
    4.6,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440201',
    'MacBook Pro 14-inch M2 Pro',
    'macbook-pro-14-m2-pro',
    'Revolutionary performance with Apple M2 Pro chip. Features Liquid Retina XDR display and exceptional battery life. Perfect for developers and creative professionals.',
    'NEW',
    '{"Processor": "Apple M2 Pro", "RAM": "16GB", "Storage": "512GB SSD", "Screen Size": "14 inch", "Graphics Card": "Integrated Graphics", "Operating System": "macOS Ventura", "Display": "Liquid Retina XDR", "Battery Life": "18 hours"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440014',
    '550e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440001',
    89,
    4.9,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440202',
    'ASUS ROG Zephyrus G14 Gaming Laptop',
    'asus-rog-zephyrus-g14',
    'Compact and powerful gaming laptop. AMD Ryzen 9 processor and NVIDIA RTX graphics deliver exceptional gaming performance in a portable 14-inch form factor.',
    'NEW',
    '{"Processor": "AMD Ryzen 9 5950X", "RAM": "32GB", "Storage": "1TB SSD", "Screen Size": "14 inch", "Graphics Card": "NVIDIA RTX 4080", "Operating System": "Windows 11 Home", "Refresh Rate": "165Hz", "RGB Lighting": "Yes"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440013',
    '550e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440001',
    67,
    4.7,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440203',
    'Lenovo ThinkPad X1 Carbon Gen 11',
    'lenovo-thinkpad-x1-carbon-gen11',
    'Ultra-lightweight business laptop with military-grade durability. Features legendary ThinkPad keyboard and long battery life for professionals on the go.',
    'NEW',
    '{"Processor": "Intel Core i7-12700K", "RAM": "16GB", "Storage": "512GB SSD", "Screen Size": "14 inch", "Graphics Card": "Integrated Graphics", "Operating System": "Windows 11 Pro", "Weight": "1.12 kg", "Battery Life": "16 hours"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440012',
    '550e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440001',
    34,
    4.5,
    NOW(),
    NOW()
);

-- DESKTOPS
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440210',
    'HP Pavilion Gaming Desktop',
    'hp-pavilion-gaming-desktop',
    'Powerful gaming desktop ready for high-performance gaming and content creation. Pre-built and ready to use out of the box.',
    'NEW',
    '{"Processor": "AMD Ryzen 7 5800X", "RAM": "32GB", "Storage": "1TB SSD", "Graphics Card": "NVIDIA RTX 3070", "Operating System": "Windows 11 Home", "Case Type": "Mid Tower", "Cooling": "Liquid Cooling"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440011',
    '550e8400-e29b-41d4-a716-446655440041',
    '550e8400-e29b-41d4-a716-446655440001',
    23,
    4.4,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440211',
    'Dell OptiPlex 7090 Tower',
    'dell-optiplex-7090',
    'Reliable business desktop with enterprise-grade security and manageability. Perfect for office environments and professional workstations.',
    'NEW',
    '{"Processor": "Intel Core i5-12400", "RAM": "16GB", "Storage": "512GB SSD", "Graphics Card": "Integrated Graphics", "Operating System": "Windows 11 Pro", "Expandability": "High", "Security": "TPM 2.0"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440010',
    '550e8400-e29b-41d4-a716-446655440041',
    '550e8400-e29b-41d4-a716-446655440001',
    18,
    4.3,
    NOW(),
    NOW()
);

-- MONITORS
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440220',
    'ASUS TUF Gaming 27" QHD Monitor',
    'asus-tuf-gaming-27-qhd',
    'Fast IPS gaming monitor with 165Hz refresh rate. Perfect for competitive gaming with HDR support and adaptive sync technology.',
    'NEW',
    '{"Screen Size": "27 inch", "Resolution": "2560x1440 (QHD)", "Refresh Rate": "165Hz", "Panel Type": "IPS", "Response Time": "1ms", "HDR": "HDR10", "Connectivity": "HDMI, DisplayPort, USB"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440013',
    '550e8400-e29b-41d4-a716-446655440033',
    '550e8400-e29b-41d4-a716-446655440001',
    56,
    4.6,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440221',
    'Dell UltraSharp 32" 4K Monitor',
    'dell-ultrasharp-32-4k',
    'Professional 4K monitor with exceptional color accuracy. Ideal for photo editing, video production, and graphic design work.',
    'NEW',
    '{"Screen Size": "32 inch", "Resolution": "3840x2160 (4K)", "Refresh Rate": "60Hz", "Panel Type": "IPS", "Color Gamut": "99% sRGB", "USB-C": "Yes", "Height Adjustable": "Yes"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440010',
    '550e8400-e29b-41d4-a716-446655440033',
    '550e8400-e29b-41d4-a716-446655440001',
    41,
    4.7,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440222',
    'Samsung Odyssey G7 Curved Gaming Monitor',
    'samsung-odyssey-g7-curved',
    'Immersive 1000R curved gaming monitor with blazing-fast 240Hz refresh rate. Experience gaming like never before.',
    'NEW',
    '{"Screen Size": "27 inch", "Resolution": "2560x1440 (QHD)", "Refresh Rate": "240Hz", "Panel Type": "VA", "Curvature": "1000R", "G-Sync": "Yes", "Response Time": "1ms"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440017',
    '550e8400-e29b-41d4-a716-446655440033',
    '550e8400-e29b-41d4-a716-446655440001',
    72,
    4.8,
    NOW(),
    NOW()
);

-- KEYBOARDS
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440230',
    'Logitech MX Keys Advanced Wireless Keyboard',
    'logitech-mx-keys-advanced',
    'Premium wireless keyboard with smart backlighting and perfect-stroke keys. Multi-device connectivity for seamless workflow.',
    'NEW',
    '{"Switch Type": "Scissor Switch", "Connectivity": "Bluetooth, USB Receiver", "Backlit": "Yes", "Battery Life": "10 days", "Multi-Device": "3 devices", "Type": "Full Size"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440019',
    '550e8400-e29b-41d4-a716-446655440050',
    '550e8400-e29b-41d4-a716-446655440001',
    128,
    4.7,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440231',
    'Razer BlackWidow V3 Mechanical Gaming Keyboard',
    'razer-blackwidow-v3',
    'Tournament-grade mechanical gaming keyboard with Razer Green switches. Durable construction built to last.',
    'NEW',
    '{"Switch Type": "Razer Green Mechanical", "Connectivity": "Wired USB", "Backlit": "RGB Chroma", "Macro Keys": "Yes", "Wrist Rest": "Included", "Type": "Full Size"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440020',
    '550e8400-e29b-41d4-a716-446655440050',
    '550e8400-e29b-41d4-a716-446655440001',
    94,
    4.6,
    NOW(),
    NOW()
);

-- MICE
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440240',
    'Logitech MX Master 3S Wireless Mouse',
    'logitech-mx-master-3s',
    'Ultimate productivity mouse with ultra-precise scrolling and ergonomic design. 8K DPI sensor for exceptional tracking.',
    'NEW',
    '{"DPI": "8000", "Connectivity": "Bluetooth, USB Receiver", "Buttons": "7", "Battery Life": "70 days", "Ergonomic": "Yes", "Silent Clicks": "Yes"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440019',
    '550e8400-e29b-41d4-a716-446655440051',
    '550e8400-e29b-41d4-a716-446655440001',
    156,
    4.8,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440241',
    'Razer DeathAdder V3 Pro Gaming Mouse',
    'razer-deathadder-v3-pro',
    'Legendary ergonomic gaming mouse perfected. 30K DPI sensor and optical switches for lightning-fast response.',
    'NEW',
    '{"DPI": "30000", "Connectivity": "Wireless 2.4GHz, Bluetooth, Wired", "Buttons": "8", "Battery Life": "90 hours", "Weight": "63g", "Sensor": "Focus Pro 30K"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440020',
    '550e8400-e29b-41d4-a716-446655440051',
    '550e8400-e29b-41d4-a716-446655440001',
    103,
    4.7,
    NOW(),
    NOW()
);

-- HEADSETS
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440250',
    'Logitech G Pro X Wireless Gaming Headset',
    'logitech-g-pro-x-wireless',
    'Professional-grade wireless gaming headset with Blue VO!CE technology. Tournament-tested with pro gamers.',
    'NEW',
    '{"Connectivity": "Wireless 2.4GHz", "Driver Size": "50mm", "Battery Life": "20 hours", "Microphone": "Blue VO!CE", "Surround Sound": "DTS:X 2.0", "Weight": "370g"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440019',
    '550e8400-e29b-41d4-a716-446655440052',
    '550e8400-e29b-41d4-a716-446655440001',
    87,
    4.6,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440251',
    'Corsair HS80 RGB Wireless Gaming Headset',
    'corsair-hs80-rgb-wireless',
    'Premium wireless gaming headset with Dolby Atmos spatial audio. All-day comfort with exceptional sound quality.',
    'NEW',
    '{"Connectivity": "Wireless 2.4GHz, Bluetooth", "Driver Size": "50mm", "Battery Life": "20 hours", "Surround Sound": "Dolby Atmos", "RGB Lighting": "Yes", "Noise Cancellation": "Yes"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440021',
    '550e8400-e29b-41d4-a716-446655440052',
    '550e8400-e29b-41d4-a716-446655440001',
    65,
    4.5,
    NOW(),
    NOW()
);

-- COMPONENTS - Graphics Cards
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440260',
    'ASUS ROG Strix GeForce RTX 4090 OC',
    'asus-rog-strix-rtx-4090',
    'Ultimate gaming graphics card with massive cooling solution. Flagship performance for 4K gaming and content creation.',
    'NEW',
    '{"GPU": "NVIDIA GeForce RTX 4090", "Memory": "24GB GDDR6X", "Boost Clock": "2640 MHz", "Cooling": "Triple Fan", "Power": "450W TDP", "Ports": "HDMI 2.1, DisplayPort 1.4a"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440013',
    '550e8400-e29b-41d4-a716-446655440061',
    '550e8400-e29b-41d4-a716-446655440001',
    145,
    4.9,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440261',
    'AMD Radeon RX 6800 XT Graphics Card',
    'amd-radeon-rx-6800-xt',
    'High-performance RDNA 2 architecture graphics card. Excellent for 4K gaming with great value.',
    'NEW',
    '{"GPU": "AMD Radeon RX 6800 XT", "Memory": "16GB GDDR6", "Boost Clock": "2250 MHz", "Cooling": "Dual Fan", "Power": "300W TDP", "Ray Tracing": "Yes"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440023',
    '550e8400-e29b-41d4-a716-446655440061',
    '550e8400-e29b-41d4-a716-446655440001',
    78,
    4.6,
    NOW(),
    NOW()
);

-- COMPONENTS - Processors
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440270',
    'Intel Core i9-13900K Desktop Processor',
    'intel-core-i9-13900k',
    'Flagship 13th Gen Intel processor with 24 cores. Ultimate performance for gaming and content creation.',
    'NEW',
    '{"Cores": "24", "Threads": "32", "Base Clock": "3.0 GHz", "Boost Clock": "5.8 GHz", "Cache": "36MB", "TDP": "125W", "Socket": "LGA1700"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440022',
    '550e8400-e29b-41d4-a716-446655440060',
    '550e8400-e29b-41d4-a716-446655440001',
    92,
    4.7,
    NOW(),
    NOW()
),
(
    '550e8400-e29b-41d4-a716-446655440271',
    'AMD Ryzen 9 5950X Desktop Processor',
    'amd-ryzen-9-5950x',
    'High-end 16-core processor with incredible multi-threaded performance. Perfect for workstation tasks.',
    'NEW',
    '{"Cores": "16", "Threads": "32", "Base Clock": "3.4 GHz", "Boost Clock": "4.9 GHz", "Cache": "64MB", "TDP": "105W", "Socket": "AM4"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440023',
    '550e8400-e29b-41d4-a716-446655440060',
    '550e8400-e29b-41d4-a716-446655440001',
    115,
    4.8,
    NOW(),
    NOW()
);

-- COMPONENTS - RAM
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440280',
    'Corsair Vengeance RGB PRO 32GB DDR4',
    'corsair-vengeance-rgb-pro-32gb',
    'High-performance DDR4 memory with dynamic RGB lighting. Optimized for peak performance on latest platforms.',
    'NEW',
    '{"Capacity": "32GB", "Memory Type": "DDR4", "Speed": "3600MHz", "CAS Latency": "18", "RGB": "Yes", "Kit": "2x16GB", "Voltage": "1.35V"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440021',
    '550e8400-e29b-41d4-a716-446655440062',
    '550e8400-e29b-41d4-a716-446655440001',
    164,
    4.7,
    NOW(),
    NOW()
);

-- COMPONENTS - Storage
INSERT INTO product (id, title, slug, description, item_condition, specifications, store_id, brand_id, category_id, seller_id, num_ratings, average_rating, created_at, updated_at) VALUES
(
    '550e8400-e29b-41d4-a716-446655440290',
    'Samsung 980 PRO 2TB NVMe SSD',
    'samsung-980-pro-2tb',
    'Flagship PCIe 4.0 NVMe SSD with exceptional performance. Lightning-fast speeds for gaming and professional workloads.',
    'NEW',
    '{"Capacity": "2TB", "Interface": "NVMe PCIe 4.0", "Form Factor": "M.2 2280", "Read Speed": "7000 MB/s", "Write Speed": "5100 MB/s", "Endurance": "1200 TBW"}',
    '550e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440017',
    '550e8400-e29b-41d4-a716-446655440063',
    '550e8400-e29b-41d4-a716-446655440001',
    198,
    4.8,
    NOW(),
    NOW()
);

-- ==============================
-- 11. PRODUCT VARIANTS
-- ==============================

-- Dell XPS 15 Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440300', '550e8400-e29b-41d4-a716-446655440200', 'DELL-XPS15-16GB-512GB', 25, 1899.99, 1699.99, 10.53, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440200', 'DELL-XPS15-32GB-1TB', 15, 2299.99, 2099.99, 8.7, false, NOW(), NOW());

-- MacBook Pro Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440201', 'MBP14-M2PRO-16GB-512GB', 18, 2499.99, 2399.99, 4.0, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440303', '550e8400-e29b-41d4-a716-446655440201', 'MBP14-M2PRO-32GB-1TB', 12, 2999.99, 2899.99, 3.33, false, NOW(), NOW());

-- ASUS ROG Zephyrus Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440304', '550e8400-e29b-41d4-a716-446655440202', 'ASUS-ROGG14-32GB-1TB', 20, 2199.99, 1999.99, 9.09, true, NOW(), NOW());

-- Lenovo ThinkPad Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440305', '550e8400-e29b-41d4-a716-446655440203', 'LENOVO-X1C11-16GB-512GB', 30, 1699.99, 1549.99, 8.82, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440306', '550e8400-e29b-41d4-a716-446655440203', 'LENOVO-X1C11-32GB-1TB', 10, 2099.99, 1899.99, 9.52, false, NOW(), NOW());

-- HP Desktop Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440307', '550e8400-e29b-41d4-a716-446655440210', 'HP-PGD-32GB-1TB', 15, 1499.99, 1349.99, 10.0, true, NOW(), NOW());

-- Dell Desktop Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440308', '550e8400-e29b-41d4-a716-446655440211', 'DELL-OPT7090-16GB-512GB', 40, 999.99, 899.99, 10.0, true, NOW(), NOW());

-- Monitor Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440309', '550e8400-e29b-41d4-a716-446655440220', 'ASUS-TUF27-QHD', 35, 399.99, 349.99, 12.5, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440310', '550e8400-e29b-41d4-a716-446655440221', 'DELL-U3223-4K', 28, 699.99, 649.99, 7.14, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440311', '550e8400-e29b-41d4-a716-446655440222', 'SAMSUNG-OG7-27-QHD', 22, 599.99, 549.99, 8.33, true, NOW(), NOW());

-- Keyboard Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440312', '550e8400-e29b-41d4-a716-446655440230', 'LOG-MXKEYS-BLK', 50, 129.99, 119.99, 7.69, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440313', '550e8400-e29b-41d4-a716-446655440231', 'RZR-BW3-GRN', 45, 139.99, 129.99, 7.14, true, NOW(), NOW());

-- Mouse Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440314', '550e8400-e29b-41d4-a716-446655440240', 'LOG-MXM3S-BLK', 60, 99.99, 89.99, 10.0, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440315', '550e8400-e29b-41d4-a716-446655440241', 'RZR-DAV3P-BLK', 55, 149.99, 139.99, 6.67, true, NOW(), NOW());

-- Headset Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440316', '550e8400-e29b-41d4-a716-446655440250', 'LOG-GPROX-WL', 40, 249.99, 229.99, 8.0, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440317', '550e8400-e29b-41d4-a716-446655440251', 'COR-HS80-WL', 35, 179.99, 159.99, 11.11, true, NOW(), NOW());

-- Graphics Card Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440318', '550e8400-e29b-41d4-a716-446655440260', 'ASUS-RTX4090-24GB', 8, 1999.99, 1899.99, 5.0, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440319', '550e8400-e29b-41d4-a716-446655440261', 'AMD-RX6800XT-16GB', 12, 649.99, 599.99, 7.69, true, NOW(), NOW());

-- Processor Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440320', '550e8400-e29b-41d4-a716-446655440270', 'INTEL-I9-13900K', 25, 589.99, 549.99, 6.78, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440321', '550e8400-e29b-41d4-a716-446655440271', 'AMD-R9-5950X', 20, 549.99, 499.99, 9.09, true, NOW(), NOW());

-- RAM Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440322', '550e8400-e29b-41d4-a716-446655440280', 'COR-VEN-32GB-DDR4', 75, 149.99, 129.99, 13.33, true, NOW(), NOW());

-- Storage Variants
INSERT INTO product_variant (id, product_id, sku, stock_quantity, mrp_price, selling_price, discount_percentage, is_default, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440323', '550e8400-e29b-41d4-a716-446655440290', 'SAM-980PRO-2TB', 50, 279.99, 249.99, 10.71, true, NOW(), NOW());

-- ==============================
-- End of Seed Data
-- ==============================
