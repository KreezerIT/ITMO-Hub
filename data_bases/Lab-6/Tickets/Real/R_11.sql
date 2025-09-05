with prodsInCategory(Category_ID, products) as (
    select distinct ps.Product_Category_ID,
                    count(*) over (partition by ps.Product_Category_ID)
    from Production.Product as p
             join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID = p.Product_Subcategory_ID
), prodsPerCustCategory(Customer_ID, Category_ID, products) as (
    select h.Customer_Id, ps.Product_Category_ID,
           row_number() over (partition by h.Customer_ID, ps.Product_Category_ID order by p.Product_ID)
    from Sales.Sales_Order_Header as h
             join Sales.Sales_Order_Detail as d on d.Sales_Order_ID = h.Sales_Order_ID
             join Production.Product as p on p.Product_ID = d.Product_ID
             join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID = p.Product_Subcategory_ID
), calculatedResults as (
    select distinct pcc.Customer_ID, pcc.Category_ID,
                    cast(max(pcc.products) over (partition by pcc.Customer_ID, pcc.Category_ID) as float) as max_products,
                    (pic.products - max(pcc.products) over (partition by pcc.Customer_ID, pcc.Category_ID)) as diff_products,
                    pic.products
    from prodsPerCustCategory as pcc
             join prodsInCategory as pic on pic.Category_ID = pcc.Category_ID
)
select Customer_ID, Category_ID,
       max_products / diff_products as result
from calculatedResults
where diff_products <> 0
order by Customer_ID;