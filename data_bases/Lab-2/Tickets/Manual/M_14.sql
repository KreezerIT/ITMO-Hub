SELECT product_subcategory_id, count(product_id) as amount FROM production.product
WHERE product_subcategory_id is not null and color is not null
GROUP BY product_subcategory_id