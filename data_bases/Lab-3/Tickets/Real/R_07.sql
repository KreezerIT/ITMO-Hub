SELECT pr.name, pc.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
WHERE pr.color = 'Blue'
GROUP BY pr.name, pc.name
HAVING count(order_qty) = 2