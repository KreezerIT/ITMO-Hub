WITH CategoryCount AS (
    SELECT
        p.product_subcategory_id AS id,
        COUNT(*) AS cnt
    FROM production.product AS p
    GROUP BY p.product_subcategory_id
)
SELECT
    p.name AS "Product",
    ps.name AS "Subcategory",
    cc.cnt AS "Amt in category"
FROM production.product AS p
         JOIN production.product_subcategory AS ps
              ON p.product_subcategory_id = ps.product_subcategory_id
         JOIN CategoryCount AS cc
              ON p.product_subcategory_id = cc.id;