SELECT customer_id
FROM sales.sales_order_header
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    GROUP BY sales_order_id
    HAVING count(distinct product_id) >=3
)
GROUP BY customer_id
HAVING count(sales_order_id)>=3