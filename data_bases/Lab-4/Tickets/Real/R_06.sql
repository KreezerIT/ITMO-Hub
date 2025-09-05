SELECT sod1.sales_order_id
FROM sales.sales_order_detail AS sod1
WHERE NOT EXISTS (
    SELECT sod2.product_id
    FROM sales.sales_order_detail AS sod2
    WHERE sod1.sales_order_id = sod2.sales_order_id
      AND unit_price <= (
        SELECT AVG(unit_price)
        FROM sales.sales_order_detail
    )
)