SELECT product_subcategory_id FROM production.product
GROUP BY product_subcategory_id
HAVING count(distinct(product_id))>5