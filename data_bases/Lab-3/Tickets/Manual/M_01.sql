SELECT p.name, pc.name
FROM production.product AS p JOIN production.product_subcategory AS ps
                                  ON p.product_subcategory_id = ps.product_subcategory_id
                             JOIN production.product_category as pc
                                  ON ps.product_category_id = pc.product_category_id
WHERE color = 'Red' and list_price > 100