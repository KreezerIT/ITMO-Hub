SELECT product_id FROM sales.sales_order_detail
WHERE order_qty > 1
GROUP BY product_id
HAVING count(order_qty) > 2