SELECT pv.name, count(*)
FROM purchasing.vendor as pv
         join purchasing.product_vendor prv
              ON pv.business_entity_id = prv.business_entity_id
GROUP BY name