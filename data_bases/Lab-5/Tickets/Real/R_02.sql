WITH total_subcategories AS (
    SELECT COUNT(DISTINCT product_subcategory_id) AS total_subcategories
    FROM production.product_subcategory
),
     customer_subcategories AS (
         SELECT
             soh.customer_id,
             COUNT(DISTINCT p.product_subcategory_id) AS customer_subcategories_count
         FROM sales.sales_order_header soh
                  JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
                  JOIN production.product p ON sod.product_id = p.product_id
         GROUP BY soh.customer_id
     ),
     qualified_customers AS (
         SELECT
             cs.customer_id
         FROM customer_subcategories cs
                  CROSS JOIN total_subcategories ts
         WHERE cs.customer_subcategories_count > ts.total_subcategories / 2
     ),
     customer_orders AS (
         SELECT
             soh.customer_id,
             COUNT(soh.sales_order_id) AS total_orders,
             AVG(soh.subtotal) AS avg_order_sum
         FROM sales.sales_order_header soh
         WHERE soh.customer_id IN (SELECT customer_id FROM qualified_customers)
         GROUP BY soh.customer_id
     )
SELECT
    co.customer_id,
    co.total_orders,
    co.avg_order_sum
FROM customer_orders co;