SELECT pr1.product_id
FROM production.product as pr1
WHERE pr1.list_price > (
    SELECT AVG(pr2.list_price)
    FROM production.product as pr2
    WHERE pr2.product_id IN (
        SELECT product_id
        FROM sales.sales_order_detail
        GROUP BY product_id
        HAVING count(order_qty)>3
    )
)