SELECT name FROM production.product
WHERE LENGTH(name) - LENGTH(REPLACE(name,'-',''))=2