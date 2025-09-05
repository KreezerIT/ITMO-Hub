SELECT pv.name, count(distinct pr.product_subcategory_id)
FROM production.product as pr
         join purchasing.product_vendor as prv
              ON pr.product_id = prv.product_id
         join purchasing.vendor as pv
              ON prv.business_entity_id = pv.business_entity_id
WHERE pr.product_subcategory_id is not null
GROUP BY pv.name
ORDER BY count(distinct pr.product_subcategory_id) desc