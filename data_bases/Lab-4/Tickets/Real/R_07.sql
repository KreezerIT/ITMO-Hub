SELECT customer_id
FROM sales.sales_order_header as soh
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    WHERE product_id IN (
        SELECT product_id
        FROM sales.sales_order_detail AS sod
        WHERE sales_order_id IN (
            SELECT sales_order_id
            FROM sales.sales_order_header
            WHERE sales_order_id IN (
                SELECT sales_order_id
                FROM sales.sales_order_detail
                GROUP BY sales_order_id
                HAVING COUNT(product_id) >= 3
            )
        )
        GROUP BY product_id
        HAVING COUNT(DISTINCT (
            SELECT soh2.customer_id
            FROM sales.sales_order_header as soh2
            WHERE soh2.sales_order_id = sod.sales_order_id
        )) >= 3
    )
    GROUP BY sales_order_id
    HAVING COUNT(product_id) >= 3
)
GROUP BY customer_id
HAVING COUNT(sales_order_id) >= 2;