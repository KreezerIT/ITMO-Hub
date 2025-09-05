SELECT listprice FROM production.product
WHERE EXTRACT(YEAR FROM sellstartdate) >= '2011' and EXTRACT(MONTH FROM sellstartdate)>='03' and EXTRACT(DAY FROM sellstartdate)>='01'
ORDER BY listprice DESC
    LIMIT 1