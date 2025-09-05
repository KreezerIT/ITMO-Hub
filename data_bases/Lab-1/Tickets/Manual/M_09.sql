SELECT name FROM production.product
WHERE (name LIKE 'D%' or name LIKE 'M%') and length(name)>3