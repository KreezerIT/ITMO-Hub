SELECT pc.name, count(distinct pr.size)
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.name