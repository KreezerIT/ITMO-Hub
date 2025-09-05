SELECT name FROM production.product
WHERE SUBSTRING(name,3,1) in ('s','r')