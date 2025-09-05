SELECT color FROM production.product
GROUP BY distinct(color)
ORDER BY count(product_id) DESC