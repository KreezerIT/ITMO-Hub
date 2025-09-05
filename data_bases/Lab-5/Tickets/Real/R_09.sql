with goodChecks(orderID) as (
    select sales_order_id from Sales.sales_order_detail as d
                                   join Production.Product as p on p.product_id=d.product_id
    group by sales_order_id
    having count(distinct(p.product_subcategory_id))>=2
)
select h.customer_id, count(*) as goodChecks from Sales.sales_order_header as h
                                                      join goodChecks as gc on gc.orderID=h.sales_order_id
group by h.customer_id