WITH t1 (id, cnt) AS (
    SELECT soh.Customer_ID, COUNT(*)
    FROM Sales.Sales_Order_Header AS soh
    GROUP BY soh.Customer_ID
), t2 (id, cnt) AS (
    SELECT soh.Customer_ID, COUNT(*)
    FROM Sales.Sales_Order_Header AS soh
             JOIN Sales.Sales_Order_Detail AS sod
                  ON soh.Sales_Order_ID = sod.Sales_Order_ID
    GROUP BY soh.Customer_ID
)
SELECT p.First_Name, p.Last_Name, t1.cnt, t2.cnt
FROM Sales.Customer AS c
         JOIN Person.Person AS p
              ON c.Person_ID = p.Business_Entity_ID
         JOIN t1
              ON t1.id = c.Customer_ID
         JOIN t2
              ON t2.id = c.Customer_ID