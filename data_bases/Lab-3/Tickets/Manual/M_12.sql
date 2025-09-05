SELECT pv.credit_rating, count(*)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
GROUP BY pv.credit_rating