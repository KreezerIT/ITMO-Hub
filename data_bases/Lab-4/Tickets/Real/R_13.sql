select customer_id
from sales.sales_order_header
where sales_order_id in(
    select sales_order_id
    from sales.sales_order_detail
    where product_id in
          (
              select product_id
              from sales.sales_order_detail
              group by product_id
              having count(*)>=3

          )
)
group by customer_id
having count(sales_order_id)>=3