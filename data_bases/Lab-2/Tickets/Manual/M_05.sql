SELECT product_id, sum(order_qty) as zakzai FROM sales.sales_order_detail
GROUP BY product_id
HAVING sum(order_qty) > 5