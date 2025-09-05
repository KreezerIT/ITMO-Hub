with totalCustomers(totalCustomers) as (
    select count(distinct customer_id) from Sales.sales_order_header
), customersPerProduct(productId, customerCount) as (
    select d.product_id, count(distinct h.Customer_ID) from Sales.sales_order_detail as d
                                                                join Sales.sales_order_header as h
                                                                     on h.sales_order_id=d.sales_order_id
    group by d.product_id
)
select distinct ProductID, cast(customerCount as float)/(select totalCustomers from totalCustomers)
from customersPerProduct