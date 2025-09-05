SELECT pr.name
FROM production.product as pr
         left join sales.sales_order_detail as sod
                   ON pr.product_id = sod.product_id
GROUP BY pr.name
HAVING count(order_qty) = 0