SELECT sales_order_id
FROM sales.sales_order_detail
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    WHERE product_id IN (
        SELECT product_id
        FROM production.product
        WHERE list_price > (SELECT AVG(list_price) FROM production.product)
    )
    GROUP BY sales_order_id
    HAVING COUNT(product_id) >2
)
GROUP BY sales_order_id
HAVING COUNT(DISTINCT product_id) = (
    SELECT COUNT(*)
    FROM sales.sales_order_detail AS sod
    WHERE sod.sales_order_id = sales.sales_order_detail.sales_order_id
      AND sod.product_id IN (
        SELECT product_id
        FROM production.product
        WHERE list_price > (SELECT AVG(list_price) FROM production.product)
    )
)