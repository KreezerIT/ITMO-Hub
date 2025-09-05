SELECT pr1.name
FROM production.product as pr1
         join production.product as pr2
              ON pr1.name = pr2.name and pr1.product_id!=pr2.product_id
WHERE pr1.name is not null