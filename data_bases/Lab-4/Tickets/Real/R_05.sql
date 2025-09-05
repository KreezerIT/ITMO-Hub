SELECT soh.customer_id
FROM sales.sales_order_header as soh
GROUP BY soh.customer_id
HAVING sum(soh.total_due) > (
    SELECT AVG(summ)
    FROM (
             SELECT sum(soh2.total_due) as summ
             FROM sales.sales_order_header as soh2
             GROUP BY soh2.customer_id
         )
)