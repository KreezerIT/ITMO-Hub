SELECT
    sod.Product_ID,
    sod.Sales_Order_ID,
    soh.Order_Date,
    sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount) AS Product_Cost,
    sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount)
        - LAG(sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount), 1, 0)
                                                                      OVER (PARTITION BY sod.Product_ID ORDER BY soh.Order_Date, sod.Sales_Order_ID) AS Dynamic
FROM Sales.Sales_Order_Detail sod
         JOIN Sales.Sales_Order_Header soh
              ON sod.Sales_Order_ID = soh.Sales_Order_ID;
