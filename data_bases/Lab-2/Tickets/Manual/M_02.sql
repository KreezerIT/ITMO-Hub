SELECT color FROM production.product
GROUP BY color
HAVING MIN(list_price) > 100