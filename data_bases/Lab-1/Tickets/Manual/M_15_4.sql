SELECT name FROM production.product
WHERE RIGHT(LEFT(name,3),1) in ('s','r')