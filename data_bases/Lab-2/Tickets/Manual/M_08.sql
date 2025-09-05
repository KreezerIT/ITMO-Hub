SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING sum(order_qty) >3
