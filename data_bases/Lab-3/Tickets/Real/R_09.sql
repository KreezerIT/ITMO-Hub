SELECT pv.name, count(pr.product_id)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
         join production.product as pr
              ON pr.product_id = prv.product_id
GROUP BY pv.name
ORDER BY count(pr.product_id)