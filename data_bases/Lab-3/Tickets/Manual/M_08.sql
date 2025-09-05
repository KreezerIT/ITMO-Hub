SELECT pr.name
FROM production.product as pr
         join purchasing.product_vendor prv
              ON pr.product_id = prv.product_id
GROUP BY pr.name
HAVING count(*)>1