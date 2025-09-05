SELECT t1.Sales_Order_ID AS OrderNum, t1.cps AS SubcatAmount, t1.cpc AS CatAmount
FROM (
         SELECT sod.Sales_Order_ID, COUNT(DISTINCT ps.Product_Subcategory_ID) AS cps, COUNT(DISTINCT ps.Product_Category_ID) AS cpc
         FROM Sales.Sales_Order_Detail AS sod
                  JOIN Production.Product AS p
                       ON p.Product_ID = sod.Product_ID
                  JOIN Production.Product_Subcategory AS ps
                       ON p.Product_Subcategory_ID = ps.Product_Subcategory_ID
         GROUP BY Sales_Order_ID
     ) AS t1
ORDER BY Sales_Order_ID