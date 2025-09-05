WITH CheckTotals AS (
    SELECT
        soh.sales_order_id,
        SUM(sod.unit_price * sod.order_qty) OVER (PARTITION BY soh.sales_order_id) AS total_without_discount,
        SUM((sod.unit_price * sod.order_qty) * (1 - sod.unit_price_discount)) OVER (PARTITION BY soh.sales_order_id) AS total_with_discount,
        SUM((sod.unit_price * sod.order_qty) * sod.unit_price_discount) OVER (PARTITION BY soh.sales_order_id) AS total_discount_amount,
        AVG(sod.unit_price_discount * 100) OVER (PARTITION BY soh.sales_order_id) AS discount_percentage
    FROM
        sales.sales_order_header AS soh
            INNER JOIN
        sales.sales_order_detail AS sod
        ON soh.sales_order_id = sod.sales_order_id
)
SELECT DISTINCT
    sales_order_id,
    total_without_discount,
    total_with_discount
FROM
    CheckTotals
WHERE
    total_discount_amount >= 1000
  AND discount_percentage <= 5
ORDER BY
    sales_order_id;