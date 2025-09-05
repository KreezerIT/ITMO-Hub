# *ЛР-5* реальные

1. Вывести на экран для каждого продукта количество его продаж, и соотношение числа покупателей этого продукта к числу покупателей, купивших товары из категории к которой относится данный товар  
```SQL
with orderInfo(CustomerID, OrderID, ProductID, CategoryID) as (
    select h.customer_id, h.sales_order_id, d.product_id, ps.product_category_id from
        Sales.sales_order_header as h
            join Sales.sales_order_detail as d
                 on d.sales_order_id=h.sales_order_id
            join Production.Product as p
                 on p.product_id=d.product_id
            join Production.product_subcategory as ps
                 on ps.product_subcategory_id=p.product_subcategory_id
), customersPerCategory(CategoryID, customers) as (
    select CategoryID, count(distinct(customerID)) from orderInfo
    group by CategoryID
), customersPerProduct(ProductID, customers) as (
    select ProductID, count(distinct(CustomerID)) from orderInfo
    group by ProductID
), salesPerProduct(ProductID, sales) as (
    select ProductID, count(distinct(OrderID)) from orderInfo
    group by ProductID
)
select distinct oi.ProductID, sales, cast(cpp.customers as float)/cpc.customers from orderInfo as oi
                                                                                         join customersPerCategory as cpc on cpc.CategoryID=oi.CategoryID
                                                                                         join customersPerProduct as cpp on cpp.ProductID=oi.ProductID
                                                                                         join salesPerProduct as spp on spp.ProductID=oi.ProductID
```
2. Найти номера покупателей, который покупали товар из более чем половины подкатегорий товаров и для них вывести информацию: номер покупателя, количество чеков, средняя сумма на один чек  
```SQL
WITH total_subcategories AS (
    SELECT COUNT(DISTINCT product_subcategory_id) AS total_subcategories
    FROM production.product_subcategory
),
     customer_subcategories AS (
         SELECT
             soh.customer_id,
             COUNT(DISTINCT p.product_subcategory_id) AS customer_subcategories_count
         FROM sales.sales_order_header soh
                  JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
                  JOIN production.product p ON sod.product_id = p.product_id
         GROUP BY soh.customer_id
     ),
     qualified_customers AS (
         SELECT
             cs.customer_id
         FROM customer_subcategories cs
                  CROSS JOIN total_subcategories ts
         WHERE cs.customer_subcategories_count > ts.total_subcategories / 2
     ),
     customer_orders AS (
         SELECT
             soh.customer_id,
             COUNT(soh.sales_order_id) AS total_orders,
             AVG(soh.subtotal) AS avg_order_sum
         FROM sales.sales_order_header soh
         WHERE soh.customer_id IN (SELECT customer_id FROM qualified_customers)
         GROUP BY soh.customer_id
     )
SELECT
    co.customer_id,
    co.total_orders,
    co.avg_order_sum
FROM customer_orders co;
```
3. Найти всех покупателей, их номера, для которых верно утверждение - они ни разу не покупали товары более чем из трех подкатегорий на один чек. Для данных покупателей вывести следующую информацию: номер покупателя, номер чека, количество подкатегорий к которым относятся товары данного чека, и количество подкатегорий, из которых покупатель приобретал товары за все покупки  
```SQL
WITH CheckSubcategories AS (
    SELECT soh.customer_id,
           soh.sales_order_id,
           COUNT(DISTINCT p.product_subcategory_id) AS check_subcategory_count
    FROM sales.sales_order_header soh
             JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
             JOIN production.product p ON sod.product_id = p.product_id
    GROUP BY soh.customer_id, soh.sales_order_id
),
     CustomerTotalSubcategories AS (
         SELECT soh.customer_id,
                COUNT(DISTINCT p.product_subcategory_id) AS total_subcategory_count
         FROM sales.sales_order_header soh
                  JOIN sales.sales_order_detail sod ON soh.sales_order_id = sod.sales_order_id
                  JOIN production.product p ON sod.product_id = p.product_id
         GROUP BY soh.customer_id
     ),
     GoodCustomers AS (
         SELECT customer_id
         FROM CheckSubcategories
         GROUP BY customer_id
         HAVING MAX(check_subcategory_count) <= 3
     )

SELECT gc.customer_id,
       cs.sales_order_id,
       cs.check_subcategory_count,
       cts.total_subcategory_count
FROM GoodCustomers gc
         JOIN CheckSubcategories cs ON gc.customer_id = cs.customer_id
         JOIN CustomerTotalSubcategories cts ON gc.customer_id = cts.customer_id;
```
4. Вывести на экран следующую информацию: название товара, название категории к которой он относится, общее количество товаров в этой категории количество покупателей данного товара  
```SQL
with prodInfo(productId, productName, categoryID, categoryName, CustomerID) as (
    select p.product_id, p.Name, pc.Product_Category_ID, pc.Name, h.Customer_ID from
        Sales.sales_order_detail as d
            join Sales.sales_order_header as h
                 on d.sales_order_id=h.sales_order_id
            join Production.Product as p
                 on p.product_id=d.product_id
            join Production.product_subcategory as ps
                 on ps.product_subcategory_id=p.product_subcategory_id
            join Production.product_category as pc
                 on pc.product_category_id=ps.product_category_id
), productsPerCategory(categoryID, productsPerCategory) as (
    select categoryId, count(distinct(productId)) from prodInfo
    group by categoryID
), customersPerProduct(productID, customersPerProduct) as (
    select ProductID, count(distinct(customerID)) from prodInfo
    group by productId
)
select distinct p.ProductID, p.productName, p.categoryName, ppc.productsPerCategory,
                cpp.customersPerProduct from prodInfo as p
                                                 join productsPerCategory as ppc on ppc.categoryID=p.categoryID
                                                 join customersPerProduct as cpp on cpp.productID=p.productId
```
5. Найти для каждого чека вывести его номер, количество категорий и подкатегорий, товары из которых есть в чеке  
```SQL
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
```
6. Вывести на экран следующую информацию: название товара, название категории к которой он относится и общее количество товаров в этой категории  
```SQL
with prodInfo(ProductID, ProductName, CategoryID, CategoryName) as
         (
             select p.product_id, p.Name, pc.product_category_id, pc.Name from Production.Product as p
                                                                                   join Production.product_subcategory as ps
                                                                                        on p.product_subcategory_id=ps.product_subcategory_id
                                                                                   join Production.product_category as pc
                                                                                        on pc.product_category_id=ps.product_category_id
         ), productsPerCategory(CategoryID, prodCount) as (
    select CategoryID, count(*) from prodInfo
    group by CategoryID
)
select ProductName, CategoryName, ppc.prodCount
from prodInfo as pinfo
         join productsPerCategory as ppc
              on ppc.CategoryID=pinfo.CategoryID
```
7. Вывести на экран следующую информацию: название товара, название подкатегории к которой он относится и общее количество товаров в этой подкатегории  
```SQL
WITH CategoryCount AS (
    SELECT
        p.product_subcategory_id AS id,
        COUNT(*) AS cnt
    FROM production.product AS p
    GROUP BY p.product_subcategory_id
)
SELECT
    p.name AS "Product",
    ps.name AS "Subcategory",
    cc.cnt AS "Amt in category"
FROM production.product AS p
         JOIN production.product_subcategory AS ps
              ON p.product_subcategory_id = ps.product_subcategory_id
         JOIN CategoryCount AS cc
              ON p.product_subcategory_id = cc.id;
```
8. Найти для каждого товара соотношение количества покупателей купивших товар к общему количеству покупателей совершавших когда-либо покупки  
```SQL
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
```
9. Найти для каждого покупателя количество чеков, где присутствуют товары минимум из двух подкатегорий товаров  
```SQL
with goodChecks(orderID) as (
    select sales_order_id from Sales.sales_order_detail as d
                                   join Production.Product as p on p.product_id=d.product_id
    group by sales_order_id
    having count(distinct(p.product_subcategory_id))>=2
)
select h.customer_id, count(*) as goodChecks from Sales.sales_order_header as h
                                                      join goodChecks as gc on gc.orderID=h.sales_order_id
group by h.customer_id
```
10. Вывести на экран следующую информацию: Номер покупателя, название такого товара который он купил три раза, и название такого товара который он купил пять раз  
```SQL
WITH temp (nam,prod,con) as (
    SELECT customer_id,b.name,count(f.sales_order_id)
    FROM sales.sales_order_header as g
             JOIN sales.sales_order_detail as f
                  ON g.sales_order_id = f.sales_order_id
             JOIN production.product as b
                  ON f.product_id = b.product_id
    GROUP BY customer_id, b.name
    HAVING count(f.sales_order_id)=3
),
     temp2 (nam,prod,con) as (
         SELECT customer_id,b.name,count(f.sales_order_id)
         FROM sales.sales_order_header as g
                  JOIN sales.sales_order_detail as f
                       ON g.sales_order_id = f.sales_order_id
                  JOIN production.product as b
                       ON f.product_id = b.product_id
         GROUP BY customer_id, b.name
         HAVING count(f.sales_order_id)=5
     )
SELECT distinct a.customer_id, b.prod,b.con, c.prod,c.con
FROM sales.sales_order_header as a
         JOIN temp as b
              ON a.customer_id = b.nam
         JOIN temp2 as c
              ON a.customer_id = c.nam
```
11. Вывести на экран следующую информацию: название товара, название подкатегории к которой он относится и общее количество товаров в этой подкатегории, общее количество товаров того же цвета  
```SQL
with productInfo(productId, productName, productColor, subcatId, subcatName) as (
    select p.product_id, p.Name, p.Color, ps.Product_Subcategory_ID, ps.Name from Production.Product
                                                                                      as p
                                                                                      join Production.product_subcategory as ps
                                                                                           on p.product_subcategory_id=ps.product_subcategory_id
), productsPerSubcat(subcatId, productsPerSubcat) as (
    select subcatId, count(productId) from productInfo
    group by subcatId
), productsPerColor(productColor, productsPerColor) as (
    select productColor, count(productId) from productInfo
    group by productColor
)
select productName, subcatName, productsPerSubcat, productsPerColor from productInfo as pin
                                                                             join productsPerSubcat as pps on pin.subcatId=pps.subcatId
                                                                             join productsPerColor as ppc on pin.productColor=ppc.productColor
```
12. Вывести на экран для каждого продукта название, кол-во его продаж, общее число покупателей этого продукта, название подкатегории, к которой данный продукт относится  
```SQL
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
```
13. Найти для каждого чека и вывести его номер, количество категорий и подкатегорий, товары из которых есть в чеке  
```SQL
SELECT t1.Sales_Order_ID AS OrderNum, t1.cps AS SubcatAmount, t1.cpc AS CatAmount
FROM (
         SELECT sod.Sales_Order_ID, COUNT(DISTINCT ps.Product_Subcategory_ID) AS cps, COUNT(DISTINCT ps.Product_Category_ID) AS cpc
         FROM Sales.Sales_Order_Detail AS sod
                  JOIN Production.Product AS p
                       ON p.Product_ID = sod.Product_ID
                  JOIN Production.Product_Subcategory AS ps
                       ON p.Product_Subcategory_ID = ps.Product_Subcategory_ID
         GROUP BY Sales_Order_ID
     ) AS t1
ORDER BY Sales_Order_ID
```
14. Вывести на экран номера покупателей, количество купленных ими товаров, и количество чеков, которые у них были  
```SQL
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
```
15. Найти для каждого покупателя количество чеков, где присутствуют товары минимум из двух подкатегорий товаров  
```SQL
with t1(Productid, ProductSubcatID)as
         (select p.Product_ID, psc.Product_Subcategory_ID
          from Production.Product as p
                   join Production.Product_Subcategory as psc
                        on p.Product_Subcategory_ID = psc.Product_Subcategory_ID),
     T(SalesOrder, Subcat) as
         (select sod.Sales_Order_ID, t1.ProductSubcatID
          from Sales.Sales_Order_Detail as sod
                   join t1
                        on sod.Product_ID = t1.Productid)
select soh.Customer_ID, count(soh.Sales_Order_ID) as "Count"
from Sales.Sales_Order_Header as soh
where soh.Sales_Order_ID in
      (select A.SalesOrder
       from T as A join T as B
                        on A.SalesOrder = B.SalesOrder
       where A.Subcat != B.Subcat
group by A.SalesOrder)
group by soh.Customer_ID
```
16. Найти всех покупателей их номера для которых верно утверждение - они ни разу не покупали товары более чем из трёх подкатегорий на один чек для данных покупателей вывести следующую информацию: номер покупателя номер чека количество подкатегорий к которым относятся товары данного чека и количество подкатегорий из которых покупатель приобретал товары за все покупки  
```SQL
with PersonFullData (Sales_Person_ID, Sales_Order_ID, Product_ID, Product_Subcategory_ID)
         as (
        select soh.Sales_Person_ID, sod.Sales_Order_ID, p.Product_ID, p.Product_Subcategory_ID
        from sales.Sales_Order_Detail as sod
                 join sales.Sales_Order_Header as soh
                      on sod.Sales_Order_ID=soh.Sales_Order_ID
                 join Production.Product as p
                      on sod.Product_ID=p.Product_ID
        group by soh.Sales_Person_ID, sod.Sales_Order_ID, p.Product_ID, p.Product_Subcategory_ID
        having soh.Sales_Person_ID is not NULL),


     OrderIdWithCategoryCount (SalesOrderID, AmountCategory)
         as (
         select pfd.Sales_Order_ID, Count(distinct pfd.Product_Subcategory_ID) as AmountCategory
         from PersonFullData as pfd
         group by pfd.Sales_Order_ID
     ),
     AllCategoryByCustomer (SalesPersonID, AllCategory)
         as (
         select pfd2.Sales_Person_ID, count(distinct pfd2.Product_Subcategory_ID) as AllCategory
         from PersonFullData as pfd2
         group by pfd2.Sales_Person_ID
     )


select pfd1.Sales_Person_ID, pfd1.Sales_Order_ID, oiwcc.AmountCategory, acbc.AllCategory
from PersonFullData as pfd1
         join OrderIdWithCategoryCount as oiwcc
              on pfd1.Sales_Order_ID=oiwcc.SalesOrderID
         join  AllCategoryByCustomer as acbc
               on pfd1.Sales_Person_ID=acbc.SalesPersonID
group by pfd1.Sales_Order_ID, pfd1.Sales_Person_ID,  oiwcc.AmountCategory, acbc.AllCategory
having oiwcc.AmountCategory <= 3
order by acbc.AllCategory
```
17. Найти номера покупателей, у которых все купленные ими товары были куплены как минимум дважды, т.е на два разных чека  
```SQL
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
```
18. Вывести на экран имена покупателей(ФИО), кол-во купленных ими товаров, и кол-во чеков, которые у них были  
```SQL
WITH t1 (id, cnt) AS (
    SELECT soh.Customer_ID, COUNT(*)
    FROM Sales.Sales_Order_Header AS soh
    GROUP BY soh.Customer_ID
), t2 (id, cnt) AS (
    SELECT soh.Customer_ID, COUNT(*)
    FROM Sales.Sales_Order_Header AS soh
             JOIN Sales.Sales_Order_Detail AS sod
                  ON soh.Sales_Order_ID = sod.Sales_Order_ID
    GROUP BY soh.Customer_ID
)
SELECT p.First_Name, p.Last_Name, t1.cnt, t2.cnt
FROM Sales.Customer AS c
         JOIN Person.Person AS p
              ON c.Person_ID = p.Business_Entity_ID
         JOIN t1
              ON t1.id = c.Customer_ID
         JOIN t2
              ON t2.id = c.Customer_ID
```
19. Вывести номера покупателей для который удовлетворяет условие что в любом чеке не было куплено более трех товаров из разных подкатегорий. для полученных покупателей вывести их айди, количество подкатегорий чека и количество подкатегорий из всех чеков  
```SQL
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
```