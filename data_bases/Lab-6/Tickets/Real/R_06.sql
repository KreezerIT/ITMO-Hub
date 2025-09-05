WITH ProductDetails AS (
    SELECT
        SOD.Sales_Order_ID,
        PS.Product_Subcategory_ID,
        PSC.Product_Category_ID
    FROM
        Sales.Sales_Order_Detail AS SOD
            INNER JOIN
        Production.Product AS P
        ON SOD.Product_ID = P.Product_ID
            INNER JOIN
        Production.Product_Subcategory AS PS
        ON P.Product_Subcategory_ID = PS.Product_Subcategory_ID
            INNER JOIN
        Production.Product_Category AS PSC
        ON PS.Product_Category_ID = PSC.Product_Category_ID
),
     DistinctCategories AS (
         SELECT DISTINCT
             Sales_Order_ID,
             Product_Category_ID
         FROM ProductDetails
     ),
     DistinctSubcategories AS (
         SELECT DISTINCT
             Sales_Order_ID,
             Product_Subcategory_ID
         FROM ProductDetails
     ),
     CategoryCounts AS (
         SELECT
             Sales_Order_ID,
             COUNT(Product_Category_ID) OVER (PARTITION BY Sales_Order_ID) AS Category_Count
         FROM DistinctCategories
     ),
     SubcategoryCounts AS (
         SELECT
             Sales_Order_ID,
             COUNT(Product_Subcategory_ID) OVER (PARTITION BY Sales_Order_ID) AS Subcategory_Count
         FROM DistinctSubcategories
     )
SELECT
    cc.Sales_Order_ID,
    MAX(cc.Category_Count) AS Category_Count,
    MAX(sc.Subcategory_Count) AS Subcategory_Count
FROM CategoryCounts cc
         JOIN SubcategoryCounts sc
              ON cc.Sales_Order_ID = sc.Sales_Order_ID
GROUP BY cc.Sales_Order_ID