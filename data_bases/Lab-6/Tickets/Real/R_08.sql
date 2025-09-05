select p.Name, pc.Name,
       count(*) over (partition by pc.Product_Category_ID) as prods_cat
from Production.Product as p
         join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID=p.Product_Subcategory_ID
         join Production.Product_Category as pc on pc.Product_Category_ID=ps.Product_Category_ID