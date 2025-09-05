SELECT listprice FROM production.product
WHERE sellstartdate >= '2011-03-01'
ORDER BY listprice DESC
LIMIT 1