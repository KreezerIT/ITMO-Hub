with Customers(CustomerID, ProductID) as (
    select h.customer_id, d.product_id from Sales.sales_order_header as h
                                                join Sales.sales_order_detail as d
                                                     on d.sales_order_id=h.sales_order_id
    group by h.Customer_id, d.product_id
    having count(*)=1
) select distinct customer_id from Sales.sales_order_header
where customer_id not in (
    select sc.CustomerID from Customers as sc
)