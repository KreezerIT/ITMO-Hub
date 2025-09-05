SELECT pc.name
FROM production.product_category as pc
WHERE pc.product_category_id = (
    SELECT ps.product_category_id
    FROM production.product_subcategory as ps
    WHERE ps.product_subcategory_id = (
        SELECT pr.product_subcategory_id
        FROM production.product as pr
        WHERE product_id = (
            SELECT sod.product_id
            FROM sales.sales_order_detail as sod
            GROUP BY sod.product_id
            ORDER BY count(sod.sales_order_id) desc
    limit 1
    )
    )
    )