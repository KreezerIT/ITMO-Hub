SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING max(order_qty) = 1