SELECT pc.name, count(*)
FROM production.product as p JOIN production.product_subcategory as ps
                                  ON p.product_subcategory_id = ps.product_subcategory_id
                             JOIN production.product_category as pc
                                  ON ps.product_category_id = pc.product_category_id
GROUP BY pc.product_category_id