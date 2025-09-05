SELECT product_subcategory_id, count(*) as amount FROM production.product
GROUP BY product_subcategory_id