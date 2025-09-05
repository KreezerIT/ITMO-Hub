SELECT product_subcategory_id FROM production.product
WHERE color = 'Red'
GROUP BY product_subcategory_id
HAVING count(*)>=5