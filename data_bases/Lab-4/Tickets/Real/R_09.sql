SELECT distinct name
FROM production.product_category as pc
WHERE pc.product_category_id IN (
    SELECT product_category_id
    FROM production.product_subcategory as ps
    WHERE ps.product_subcategory_id IN (
        SELECT pr.product_subcategory_id
        FROM production.product as pr
        WHERE color = 'Red'
        GROUP BY pr.product_subcategory_id
        HAVING count(*) >=3
    )
)