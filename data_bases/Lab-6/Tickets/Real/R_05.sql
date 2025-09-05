SELECT
    p.product_id,
    COUNT(sod.sales_order_id) AS total_sales,
    COUNT(DISTINCT soh.customer_id) AS product_customer_count,
    COUNT(DISTINCT soh.customer_id) * 1.0 /
    SUM(COUNT(DISTINCT soh.customer_id)) OVER (PARTITION BY pc.product_category_id) AS CustomerRatio
FROM sales.sales_order_detail AS sod
         JOIN sales.sales_order_header AS soh
              ON sod.sales_order_id = soh.sales_order_id
         JOIN production.product AS p
              ON sod.product_id = p.product_id
         JOIN production.product_subcategory AS ps
              ON p.product_subcategory_id = ps.product_subcategory_id
         JOIN production.product_category AS pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY p.product_id, pc.product_category_id;
