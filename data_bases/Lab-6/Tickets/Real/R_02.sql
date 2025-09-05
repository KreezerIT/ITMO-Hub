select
    sales_order_id,
    order_date,
    sum(order_qty * unit_price) / sum(sum(order_qty * unit_price)) over (
    partition by
        extract(year from order_date),
        extract(month from order_date)
        ) * 100 as order_percentage
from sales.sales_order_detail as ssod
         join sales.sales_order_header as ssoh
              using (sales_order_id)
group by ssod.sales_order_id, ssoh.order_date;