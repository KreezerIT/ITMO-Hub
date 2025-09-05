with orderInfo(CustomerID, OrderID, ProductID) as (
    select h.customer_id, h.sales_order_id, d.product_id from Sales.sales_order_header as h
                                                                  join Sales.sales_order_detail as d
                                                                       on d.sales_order_id=h.sales_order_id
), productsPerCustomer(CustomerID, productCount) as (
    select CustomerID, count(distinct ProductID) from orderInfo
    group by CustomerID
), ordersPerCustomer(CustomerID, orderCount) as (
    select CustomerID, count(distinct OrderID) from orderInfo
    group by CustomerID
)
select distinct oi.CustomerID, productCount, orderCount from orderInfo as oi
                                                                 join productsPerCustomer as ppc
                                                                      on oi.CustomerID=ppc.CustomerID
                                                                 join ordersPerCustomer as opc
                                                                      on oi.CustomerID=opc.CustomerID