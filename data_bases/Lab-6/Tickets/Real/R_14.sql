WITH RankedSales AS (
    SELECT
        p.product_id,
        soh.sales_order_id,
        soh.order_date,
        sod.unit_price,
        ROW_NUMBER() OVER (PARTITION BY p.product_id ORDER BY soh.order_date) AS sale_rank
    FROM
        production.product AS p
            INNER JOIN
        sales.sales_order_detail AS sod
        ON p.product_id = sod.product_id
            INNER JOIN
        sales.sales_order_header AS soh
        ON sod.sales_order_id = soh.sales_order_id
),
     PriceComparison AS (
         SELECT
             curr.product_id,
             curr.sales_order_id,
             curr.order_date,
             curr.unit_price AS current_price,
             curr.unit_price - prev.unit_price AS price_difference
         FROM
             RankedSales AS curr
                 LEFT JOIN
             RankedSales AS prev
             ON curr.product_id = prev.product_id
                 AND curr.sale_rank = prev.sale_rank + 1
     )
SELECT
    pc.product_id,
    pc.sales_order_id,
    pc.order_date,
    pc.current_price AS sale_price,
    pc.price_difference AS price_change
FROM
    PriceComparison AS pc
ORDER BY
    pc.product_id,
    pc.order_date;