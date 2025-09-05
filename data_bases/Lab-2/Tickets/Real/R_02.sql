SELECT color FROM production.product
GROUP BY color
HAVING count(*)>=2 and count(*)<=5