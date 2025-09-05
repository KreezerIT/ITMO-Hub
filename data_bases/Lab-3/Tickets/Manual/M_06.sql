SELECT ps.name, max(list_price)
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
WHERE color = 'Red'
GROUP BY ps.product_subcategory_id