with allbey as(
    select soh.sales_order_id, soh.customer_id, count( distinct p.product_subcategory_id) as cou
    from sales.sales_order_header as soh
             join sales.sales_order_detail as sod
                  on soh.sales_order_id = sod.sales_order_id
             join production.product as p
                  on p.product_id = sod.product_id
    GROUP by soh.sales_order_id, soh.customer_id
), sumall as(
    select customer_id, sum(cou) as sumall from allbey
    GROUP by customer_id
)

SELECT a.customer_id, a.sales_order_id, a.cou, s.sumall from allbey as a
                                                                 join sumall as s
                                                                      on a.customer_id = s.customer_id
where cou <= 3
group by a.customer_id, a.sales_order_id, a.cou, s.sumall