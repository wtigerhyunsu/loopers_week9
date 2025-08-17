-- MySQL 8.0 이상 버전에서 작동
SET @@cte_max_recursion_depth = 1000000;

-- 10만 개의 brand 더미 데이터 삽입
INSERT INTO brand (user_id, name, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 100000
)
SELECT
    CONCAT('user', n) AS user_id,
    CONCAT('Brand Name ', n) AS name,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS created_at,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS updated_at
FROM numbers;

-- 100만 개의 product 더미 데이터 삽입
INSERT INTO product (brand_id, name, price, description, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000000
)
SELECT
    FLOOR(1 + RAND() * 100000) AS brand_id, -- 브랜드는 1~10만 랜덤
    CONCAT('Product Name ', n) AS name,
    FLOOR(100 + RAND() * 2000000) * 100 AS price,
    CONCAT('Description for Product ', n) AS description,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS created_at,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS updated_at
FROM numbers;

-- 100만 개의 stock 더미 데이터 삽입
INSERT INTO stock (product_id, stock, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000000
)
SELECT
    n AS product_id,
    FLOOR(10 + RAND() * 2000000) AS stock,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS created_at,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS updated_at
FROM numbers;

-- 100만 개의 product_status 더미 데이터 삽입
INSERT INTO product_status (product_id, like_count, created_at, updated_at)
WITH RECURSIVE numbers AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000000
)
SELECT
    n AS product_id,
    FLOOR(RAND() * 2000000) AS like_count,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS created_at,
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 300 * 24 * 3600), NOW()) AS updated_at
FROM numbers;
