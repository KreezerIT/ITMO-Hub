with productInfo(productId, productName, subcatId, subcatName, customerId, orderId) as (
    select p.product_id, p.Name, ps.product_subcategory_id, ps.Name, h.customer_id, h.sales_order_id
    from Production.Product as p
             join Production.product_subcategory as ps
                  on p.product_subcategory_id=ps.product_subcategory_id
             join Sales.sales_order_detail as d
                  on d.product_id=p.product_id
             join Sales.sales_order_header as h
                  on h.sales_order_id=d.sales_order_id
), CountPerProduct(productId, customerCount, orderCount) as (
    select productId, count(distinct(customerId)), count(distinct(orderId)) from productInfo
    group by productId
)
select distinct pin.productId, productName, subcatName, orderCount, customerCount from productInfo
                                                                                           as pin
                                                                                           join countPerProduct as cpp on cpp.productId=pin.productId