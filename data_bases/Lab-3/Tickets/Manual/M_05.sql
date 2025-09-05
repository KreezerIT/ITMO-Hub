SELECT ps.name
FROM production.product as pr JOIN production.product_subcategory as ps
                                   ON pr.product_subcategory_id = ps.product_subcategory_id
GROUP BY ps.product_subcategory_id
ORDER BY count(*) desc
    limit 3