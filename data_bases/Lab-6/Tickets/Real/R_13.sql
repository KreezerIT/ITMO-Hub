WITH RankedPrices AS (
    SELECT
        p.product_id,
        p.name AS product_name,
        sod.unit_price,
        soh.order_date,
        ROW_NUMBER() OVER (PARTITION BY p.product_id ORDER BY soh.order_date) AS price_rank
    FROM
        production.product AS p
            INNER JOIN
        sales.sales_order_detail AS sod
        ON p.product_id = sod.product_id
            INNER JOIN
        sales.sales_order_header AS soh
        ON sod.sales_order_id = soh.sales_order_id
),
     PriceTrends AS (
         SELECT
             curr.product_id,
             curr.product_name,
             curr.unit_price AS current_price,
             curr.order_date AS current_date,
    curr.unit_price - prev.unit_price AS price_change
FROM
    RankedPrices AS curr
    LEFT JOIN
    RankedPrices AS prev
ON curr.product_id = prev.product_id
    AND curr.price_rank = prev.price_rank + 1
    )
SELECT
    product_id,
    product_name,
    current_date,
    current_price,
    price_change
FROM
    PriceTrends
ORDER BY
    product_id,
    current_date;