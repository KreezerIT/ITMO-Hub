SELECT distinct customer_id
FROM sales.sales_order_header as soh
WHERE customer_id NOT IN (
    SELECT customer_id
    FROM sales.sales_order_header as soh2
    WHERE sales_order_id IN (
        SELECT distinct sales_order_id
        FROM sales.sales_order_detail as sod
        GROUP BY sales_order_id, product_id
        HAVING count(product_id) >1
    )
)