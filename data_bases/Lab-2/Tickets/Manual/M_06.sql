SELECT customer_id FROM sales.sales_order_header
GROUP BY customer_id, order_date
HAVING count(sales_order_id) >1