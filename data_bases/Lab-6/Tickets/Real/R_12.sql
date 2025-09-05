with t AS (
    SELECT DISTINCT
        pp.product_id,
        sod.unit_price AS original_price,
        sod.unit_price_discount AS discount_amount,
        (sod.unit_price - (sod.unit_price_discount * sod.unit_price)) AS price_with_discount,
        (sod.unit_price - (sod.unit_price - (sod.unit_price_discount * sod.unit_price))) AS total_discount,
        SUM(sod.order_qty) OVER (PARTITION BY pp.product_id) AS total_sales
    FROM
        production.product pp
            JOIN
        sales.sales_order_detail sod ON pp.product_id = sod.product_id
)
SELECT
    DISTINCT
    product_id,
    original_price,
    discount_amount,
    price_with_discount,
    total_discount,
    total_sales
FROM t
WHERE total_sales > 5