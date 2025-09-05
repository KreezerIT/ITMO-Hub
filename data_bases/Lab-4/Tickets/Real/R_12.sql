SELECT name
FROM production.product_subcategory
WHERE product_subcategory_id IN (
    SELECT product_subcategory_id
    FROM production.product
    GROUP BY product_subcategory_id
)
GROUP BY name
ORDER BY count(*) desc
    LIMIT 1