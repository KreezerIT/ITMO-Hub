SELECT product_id FROM sales.sales_order_detail
WHERE unit_price < 100
GROUP BY product_id
ORDER BY count(*) desc
LIMIT 1