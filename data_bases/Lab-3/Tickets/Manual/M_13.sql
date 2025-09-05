SELECT pv.name, count(*)
FROM purchasing.product_vendor as prv
         join purchasing.vendor as pv
              ON prv.business_entity_id = pv.business_entity_id
GROUP BY pv.name
ORDER BY count(*) desc
    limit 3