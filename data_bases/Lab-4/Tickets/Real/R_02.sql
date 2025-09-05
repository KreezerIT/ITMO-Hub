SELECT name
FROM production.product as pr1
WHERE list_price > (
    SELECT AVG(list_price)
    FROM production.product as pr2
    WHERE pr1.product_subcategory_id
              = pr2.product_subcategory_id
)