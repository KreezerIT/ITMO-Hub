WITH CheckSubcategories AS (
    SELECT soh.customer_id,
           soh.sales_order_id,
           COUNT(DISTINCT p.product_subcategory_id) AS check_subcategory_count
    FROM sales.sales_order_header soh
             JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
             JOIN production.product p ON sod.product_id = p.product_id
    GROUP BY soh.customer_id, soh.sales_order_id
),
     CustomerTotalSubcategories AS (
         SELECT soh.customer_id,
                COUNT(DISTINCT p.product_subcategory_id) AS total_subcategory_count
         FROM sales.sales_order_header soh
                  JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
                  JOIN production.product p ON sod.product_id = p.product_id
         GROUP BY soh.customer_id
     ),
     GoodCustomers AS (
         SELECT customer_id
         FROM CheckSubcategories
         GROUP BY customer_id
         HAVING MAX(check_subcategory_count) <= 3
     )

SELECT gc.customer_id,
       cs.sales_order_id,
       cs.check_subcategory_count,
       cts.total_subcategory_count
FROM GoodCustomers gc
         JOIN CheckSubcategories cs ON gc.customer_id = cs.customer_id
         JOIN CustomerTotalSubcategories cts ON gc.customer_id = cts.customer_id;