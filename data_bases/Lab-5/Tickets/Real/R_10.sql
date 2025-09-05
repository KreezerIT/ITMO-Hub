WITH temp (nam,prod,con) as (
    SELECT customer_id,b.name,count(f.sales_order_id)
    FROM sales.sales_order_header as g
             JOIN sales.sales_order_detail as f
                  ON g.sales_order_id = f.sales_order_id
             JOIN production.product as b
                  ON f.product_id = b.product_id
    GROUP BY customer_id, b.name
    HAVING count(f.sales_order_id)=3
),
     temp2 (nam,prod,con) as (
         SELECT customer_id,b.name,count(f.sales_order_id)
         FROM sales.sales_order_header as g
                  JOIN sales.sales_order_detail as f
                       ON g.sales_order_id = f.sales_order_id
                  JOIN production.product as b
                       ON f.product_id = b.product_id
         GROUP BY customer_id, b.name
         HAVING count(f.sales_order_id)=5
     )
SELECT distinct a.customer_id, b.prod,b.con, c.prod,c.con
FROM sales.sales_order_header as a
         JOIN temp as b
              ON a.customer_id = b.nam
         JOIN temp2 as c
              ON a.customer_id = c.nam