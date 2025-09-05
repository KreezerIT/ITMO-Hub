WITH max_price_category AS (
    SELECT
        pc.product_category_id,
        p.product_id,
        p.list_price,
        MAX(p.list_price) OVER (PARTITION BY pc.product_category_id) AS max_price_in_category
    FROM production.product p
             JOIN production.product_subcategory ps ON p.product_subcategory_id = ps.product_subcategory_id
             JOIN production.product_category pc ON ps.product_category_id = pc.product_category_id
)
SELECT
    mp.product_category_id,
    mp.max_price_in_category,
    MIN(mp.product_id) AS product_id_with_min_number
FROM max_price_category mp
WHERE mp.list_price = mp.max_price_in_category
GROUP BY mp.product_category_id, mp.max_price_in_category
ORDER BY mp.product_category_id;