SELECT pr.name
FROM production.product as pr
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
GROUP BY pr.name
HAVING count(*) = 3 or count(*) = 5