SELECT ps.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
GROUP BY ps.name
HAVING count(*) > 5