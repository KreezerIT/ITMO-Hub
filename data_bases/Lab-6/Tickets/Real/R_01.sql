select distinct product_id,
                count(sales_order_id) over(partition by product_id) as c
from sales.sales_order_detail
order by c desc
    limit 5;