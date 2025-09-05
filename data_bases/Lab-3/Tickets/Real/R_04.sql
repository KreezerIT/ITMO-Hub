SELECT ps.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
WHERE pr.color = 'Red'
GROUP BY ps.product_subcategory_id
HAVING count(*) > 0