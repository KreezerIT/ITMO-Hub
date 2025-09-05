with prodInfo(productId, productName, categoryID, categoryName, CustomerID) as (
    select p.product_id, p.Name, pc.Product_Category_ID, pc.Name, h.Customer_ID from
        Sales.sales_order_detail as d
            join Sales.sales_order_header as h
                 on d.sales_order_id=h.sales_order_id
            join Production.Product as p
                 on p.product_id=d.product_id
            join Production.product_subcategory as ps
                 on ps.product_subcategory_id=p.product_subcategory_id
            join Production.product_category as pc
                 on pc.product_category_id=ps.product_category_id
), productsPerCategory(categoryID, productsPerCategory) as (
    select categoryId, count(distinct(productId)) from prodInfo
    group by categoryID
), customersPerProduct(productID, customersPerProduct) as (
    select ProductID, count(distinct(customerID)) from prodInfo
    group by productId
)
select distinct p.ProductID, p.productName, p.categoryName, ppc.productsPerCategory,
                cpp.customersPerProduct from prodInfo as p
                                                 join productsPerCategory as ppc on ppc.categoryID=p.categoryID
                                                 join customersPerProduct as cpp on cpp.productID=p.productId