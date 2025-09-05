SELECT COUNT(*) AS amount FROM production.product
WHERE list_price >= 30
GROUP BY color