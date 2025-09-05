SELECT name FROM production.product
WHERE POSITION('s' IN SUBSTRING(name FROM 3 FOR 1)) = 1
   or POSITION('r' IN SUBSTRING(name FROM 3 FOR 1)) = 1