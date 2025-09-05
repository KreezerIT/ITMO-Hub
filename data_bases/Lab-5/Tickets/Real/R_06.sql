with prodInfo(ProductID, ProductName, CategoryID, CategoryName) as
         (
             select p.product_id, p.Name, pc.product_category_id, pc.Name from Production.Product as p
                                                                                   join Production.product_subcategory as ps
                                                                                        on p.product_subcategory_id=ps.product_subcategory_id
                                                                                   join Production.product_category as pc
                                                                                        on pc.product_category_id=ps.product_category_id
         ), productsPerCategory(CategoryID, prodCount) as (
    select CategoryID, count(*) from prodInfo
    group by CategoryID
)
select ProductName, CategoryName, ppc.prodCount
from prodInfo as pinfo
         join productsPerCategory as ppc
              on ppc.CategoryID=pinfo.CategoryID