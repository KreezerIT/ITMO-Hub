SELECT product_id, count(*) as amount FROM sales.sales_order_detail
GROUP BY product_id