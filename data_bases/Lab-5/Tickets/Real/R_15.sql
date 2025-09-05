with t1(Productid, ProductSubcatID)as
         (select p.Product_ID, psc.Product_Subcategory_ID
          from Production.Product as p
                   join Production.Product_Subcategory as psc
                        on p.Product_Subcategory_ID = psc.Product_Subcategory_ID),
     T(SalesOrder, Subcat) as
         (select sod.Sales_Order_ID, t1.ProductSubcatID
          from Sales.Sales_Order_Detail as sod
                   join t1
                        on sod.Product_ID = t1.Productid)
select soh.Customer_ID, count(soh.Sales_Order_ID) as "Count"
from Sales.Sales_Order_Header as soh
where soh.Sales_Order_ID in
      (select A.SalesOrder
       from T as A join T as B
                        on A.SalesOrder = B.SalesOrder
       where A.Subcat != B.Subcat
group by A.SalesOrder)
group by soh.Customer_ID