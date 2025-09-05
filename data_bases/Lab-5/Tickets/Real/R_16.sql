with PersonFullData (Sales_Person_ID, Sales_Order_ID, Product_ID, Product_Subcategory_ID)
         as (
        select soh.Sales_Person_ID, sod.Sales_Order_ID, p.Product_ID, p.Product_Subcategory_ID
        from sales.Sales_Order_Detail as sod
                 join sales.Sales_Order_Header as soh
                      on sod.Sales_Order_ID=soh.Sales_Order_ID
                 join Production.Product as p
                      on sod.Product_ID=p.Product_ID
        group by soh.Sales_Person_ID, sod.Sales_Order_ID, p.Product_ID, p.Product_Subcategory_ID
        having soh.Sales_Person_ID is not NULL),


     OrderIdWithCategoryCount (SalesOrderID, AmountCategory)
         as (
         select pfd.Sales_Order_ID, Count(distinct pfd.Product_Subcategory_ID) as AmountCategory
         from PersonFullData as pfd
         group by pfd.Sales_Order_ID
     ),
     AllCategoryByCustomer (SalesPersonID, AllCategory)
         as (
         select pfd2.Sales_Person_ID, count(distinct pfd2.Product_Subcategory_ID) as AllCategory
         from PersonFullData as pfd2
         group by pfd2.Sales_Person_ID
     )


select pfd1.Sales_Person_ID, pfd1.Sales_Order_ID, oiwcc.AmountCategory, acbc.AllCategory
from PersonFullData as pfd1
         join OrderIdWithCategoryCount as oiwcc
              on pfd1.Sales_Order_ID=oiwcc.SalesOrderID
         join  AllCategoryByCustomer as acbc
               on pfd1.Sales_Person_ID=acbc.SalesPersonID
group by pfd1.Sales_Order_ID, pfd1.Sales_Person_ID,  oiwcc.AmountCategory, acbc.AllCategory
having oiwcc.AmountCategory <= 3
order by acbc.AllCategory