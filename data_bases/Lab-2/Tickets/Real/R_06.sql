SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING count(order_qty)<3 and count(distinct(sales_order_id))>3