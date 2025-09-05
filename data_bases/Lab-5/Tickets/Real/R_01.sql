with orderInfo(CustomerID, OrderID, ProductID, CategoryID) as (
    select h.customer_id, h.sales_order_id, d.product_id, ps.product_category_id from
        Sales.sales_order_header as h
            join Sales.sales_order_detail as d
                 on d.sales_order_id=h.sales_order_id
            join Production.Product as p
                 on p.product_id=d.product_id
            join Production.product_subcategory as ps
                 on ps.product_subcategory_id=p.product_subcategory_id
), customersPerCategory(CategoryID, customers) as (
    select CategoryID, count(distinct(customerID)) from orderInfo
    group by CategoryID
), customersPerProduct(ProductID, customers) as (
    select ProductID, count(distinct(CustomerID)) from orderInfo
    group by ProductID
), salesPerProduct(ProductID, sales) as (
    select ProductID, count(distinct(OrderID)) from orderInfo
    group by ProductID
)
select distinct oi.ProductID, sales, cast(cpp.customers as float)/cpc.customers from orderInfo as oi
                                                                                         join customersPerCategory as cpc on cpc.CategoryID=oi.CategoryID
                                                                                         join customersPerProduct as cpp on cpp.ProductID=oi.ProductID
                                                                                         join salesPerProduct as spp on spp.ProductID=oi.ProductID