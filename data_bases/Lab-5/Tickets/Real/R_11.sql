with productInfo(productId, productName, productColor, subcatId, subcatName) as (
    select p.product_id, p.Name, p.Color, ps.Product_Subcategory_ID, ps.Name from Production.Product
                                                                                      as p
                                                                                      join Production.product_subcategory as ps
                                                                                           on p.product_subcategory_id=ps.product_subcategory_id
), productsPerSubcat(subcatId, productsPerSubcat) as (
    select subcatId, count(productId) from productInfo
    group by subcatId
), productsPerColor(productColor, productsPerColor) as (
    select productColor, count(productId) from productInfo
    group by productColor
)
select productName, subcatName, productsPerSubcat, productsPerColor from productInfo as pin
                                                                             join productsPerSubcat as pps on pin.subcatId=pps.subcatId
                                                                             join productsPerColor as ppc on pin.productColor=ppc.productColor