select p.Name, ps.Name,
       count(*) over (partition by ps.Product_Subcategory_ID) as prods_cat
from Production.Product as p
         join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID=p.Product_Subcategory_ID