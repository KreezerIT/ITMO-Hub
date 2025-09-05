SELECT sales_order_id FROM sales.sales_order_detail
GROUP BY sales_order_id
HAVING count(*)>3