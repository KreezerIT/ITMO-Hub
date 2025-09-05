SELECT sales_order_id FROM sales.sales_order_detail
GROUP BY sales_order_id
ORDER BY count(distinct(product_id)) DESC
LIMIT 1