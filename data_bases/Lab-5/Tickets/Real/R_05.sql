with orderInfo(OrderID, CategoryID, SubcatID) as (
    select d.sales_order_id, ps.product_category_id, ps.product_subcategory_id from
        Sales.sales_order_detail as d
            join Production.Product as p
                 on p.product_id=d.product_id
            join production.product_subcategory as ps
                 on ps.product_subcategory_id=p.product_subcategory_id
), categoryPerOrder(OrderID, categoryPerOrder) as (
    select OrderID, count(distinct(CategoryID)) from orderInfo
    group by OrderID
), subCatPerOrder(OrderID, subCatPerOrder) as (
    select OrderID, count(distinct(SubcatID)) from orderInfo
    group by OrderID
)
select distinct oi.OrderID, categoryPerOrder, subCatPerOrder from orderInfo as oi
                                                                      join categoryPerOrder as cpo on cpo.OrderID=oi.OrderID
                                                                      join subCatPerOrder as spo on spo.OrderID=oi.OrderID