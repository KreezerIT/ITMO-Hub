SELECT sales_order_id
FROM sales.sales_order_detail
WHERE product_id IN (
    SELECT product_id
    FROM production.product AS p
    WHERE product_subcategory_id IN (
        SELECT product_subcategory_id FROM production.product_subcategory
        WHERE product_category_id IN (
            SELECT product_category_id FROM production.product_category AS pc
            WHERE pc.name = 'Accessories'
        )
    ) AND list_price > (
        SELECT AVG(p.list_price)
        FROM production.product AS p
        WHERE product_subcategory_id IN (
            SELECT product_subcategory_id
            FROM production.product_subcategory
        )
        GROUP BY product_subcategory_id
        ORDER BY 1 DESC
    LIMIT 1
    )
    )