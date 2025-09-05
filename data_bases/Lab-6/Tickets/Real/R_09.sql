SELECT
    sod.product_id,
    COUNT(DISTINCT soh.customer_id) * 1.0 / SUM(COUNT(DISTINCT soh.customer_id)) OVER () AS customer_ratio
FROM sales.sales_order_detail AS sod
         JOIN sales.sales_order_header AS soh
              ON sod.sales_order_id = soh.sales_order_id
GROUP BY sod.product_id;