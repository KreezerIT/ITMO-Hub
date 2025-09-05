# *ЛР-6* реальные

1. Найти 5 самых продаваемых товаров, вывести их номер и количество продаж  
```SQL
select distinct product_id,
                count(sales_order_id) over(partition by product_id) as c
from sales.sales_order_detail
order by c desc
    limit 5;
```
2. Найти для каждого чека его номер, дату его оформления и процент стоимости данного чека от месячных продаж  
```SQL
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
```
3. Вывести на экран следующую информацию: номер категории, максимальную цену в этой категории, номер товара с наименьшим номером из этой категории с такой ценой  
```SQL
WITH max_price_category AS (
    SELECT
        pc.product_category_id,
        p.product_id,
        p.list_price,
        MAX(p.list_price) OVER (PARTITION BY pc.product_category_id) AS max_price_in_category
    FROM production.product p
             JOIN production.product_subcategory ps ON p.product_subcategory_id = ps.product_subcategory_id
             JOIN production.product_category pc ON ps.product_category_id = pc.product_category_id
)
SELECT
    mp.product_category_id,
    mp.max_price_in_category,
    MIN(mp.product_id) AS product_id_with_min_number
FROM max_price_category mp
WHERE mp.list_price = mp.max_price_in_category
GROUP BY mp.product_category_id, mp.max_price_in_category
ORDER BY mp.product_category_id;
```
4. Для каждого продукта найти его ID, ID чека, время покупки, его стоимость и динамику стоимости между предыдущим чеком по дате и текущим  
```SQL
SELECT
    sod.Product_ID,
    sod.Sales_Order_ID,
    soh.Order_Date,
    sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount) AS Product_Cost,
    sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount)
        - LAG(sod.order_qty * sod.unit_price * (1 - sod.unit_price_discount), 1, 0)
                                                                      OVER (PARTITION BY sod.Product_ID ORDER BY soh.Order_Date, sod.Sales_Order_ID) AS Dynamic
FROM Sales.Sales_Order_Detail sod
         JOIN Sales.Sales_Order_Header soh
              ON sod.Sales_Order_ID = soh.Sales_Order_ID;

```
5. Вывести на экран, для каждого продукта, количество его продаж, и соотношение числа покупателей этого продукта, к числу покупателей, купивших товары из категории, к которой относится данный товар  
```SQL
SELECT
    p.product_id,
    COUNT(sod.sales_order_id) AS total_sales,
    COUNT(DISTINCT soh.customer_id) AS product_customer_count,
    COUNT(DISTINCT soh.customer_id) * 1.0 /
    SUM(COUNT(DISTINCT soh.customer_id)) OVER (PARTITION BY pc.product_category_id) AS CustomerRatio
FROM sales.sales_order_detail AS sod
         JOIN sales.sales_order_header AS soh
              ON sod.sales_order_id = soh.sales_order_id
         JOIN production.product AS p
              ON sod.product_id = p.product_id
         JOIN production.product_subcategory AS ps
              ON p.product_subcategory_id = ps.product_subcategory_id
         JOIN production.product_category AS pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY p.product_id, pc.product_category_id;

```
6. Найти для каждого чека его номер, количество категорий и подкатегорий, товары из которых есть в чеке  
```SQL
WITH ProductDetails AS (
    SELECT
        SOD.Sales_Order_ID,
        PS.Product_Subcategory_ID,
        PSC.Product_Category_ID
    FROM
        Sales.Sales_Order_Detail AS SOD
            INNER JOIN
        Production.Product AS P
        ON SOD.Product_ID = P.Product_ID
            INNER JOIN
        Production.Product_Subcategory AS PS
        ON P.Product_Subcategory_ID = PS.Product_Subcategory_ID
            INNER JOIN
        Production.Product_Category AS PSC
        ON PS.Product_Category_ID = PSC.Product_Category_ID
),
     DistinctCategories AS (
         SELECT DISTINCT
             Sales_Order_ID,
             Product_Category_ID
         FROM ProductDetails
     ),
     DistinctSubcategories AS (
         SELECT DISTINCT
             Sales_Order_ID,
             Product_Subcategory_ID
         FROM ProductDetails
     ),
     CategoryCounts AS (
         SELECT
             Sales_Order_ID,
             COUNT(Product_Category_ID) OVER (PARTITION BY Sales_Order_ID) AS Category_Count
         FROM DistinctCategories
     ),
     SubcategoryCounts AS (
         SELECT
             Sales_Order_ID,
             COUNT(Product_Subcategory_ID) OVER (PARTITION BY Sales_Order_ID) AS Subcategory_Count
         FROM DistinctSubcategories
     )
SELECT
    cc.Sales_Order_ID,
    MAX(cc.Category_Count) AS Category_Count,
    MAX(sc.Subcategory_Count) AS Subcategory_Count
FROM CategoryCounts cc
         JOIN SubcategoryCounts sc
              ON cc.Sales_Order_ID = sc.Sales_Order_ID
GROUP BY cc.Sales_Order_ID
```
7. Вывести на экран следующую информацию: название товара, название подкатегории к которой он относится и общее количество товаров в этой подкатегории  
```SQL
select p.Name, ps.Name,
       count(*) over (partition by ps.Product_Subcategory_ID) as prods_cat
from Production.Product as p
         join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID=p.Product_Subcategory_ID
```
8. Вывести на экран следующую информацию: название товара, название категории к которой он относится и общее количество товаров в этой категории  
```SQL
select p.Name, pc.Name,
       count(*) over (partition by pc.Product_Category_ID) as prods_cat
from Production.Product as p
         join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID=p.Product_Subcategory_ID
         join Production.Product_Category as pc on pc.Product_Category_ID=ps.Product_Category_ID
```
9. Найти для каждого товара соотношение количества покупателей купивших товар к общему количеству покупателей совершавших когда-либо покупки  
```SQL
SELECT
    sod.product_id,
    COUNT(DISTINCT soh.customer_id) * 1.0 / SUM(COUNT(DISTINCT soh.customer_id)) OVER () AS customer_ratio
FROM sales.sales_order_detail AS sod
         JOIN sales.sales_order_header AS soh
              ON sod.sales_order_id = soh.sales_order_id
GROUP BY sod.product_id;
```
10. Найти для каждого покупателя все чеки, и для каждого чека вывести информацию: номер покупателя, номер чека, и сумма всех затрат этого покупателя с момента первой покупки и до данного момента  
```SQL
select Customer_ID, Sales_Order_ID,
       sum(SubTotal) over (partition by Customer_ID order by Order_Date
rows between unbounded preceding and current row)
from Sales.Sales_Order_Header
order by Customer_ID, sales_order_id
```
11. Найти для каждого покупателя соотношение количества купленных им товаров из каждой категории, к количеству не купленных им товаров той же категории  
```SQL
with prodsInCategory(Category_ID, products) as (
    select distinct ps.Product_Category_ID,
                    count(*) over (partition by ps.Product_Category_ID)
    from Production.Product as p
             join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID = p.Product_Subcategory_ID
), prodsPerCustCategory(Customer_ID, Category_ID, products) as (
    select h.Customer_Id, ps.Product_Category_ID,
           row_number() over (partition by h.Customer_ID, ps.Product_Category_ID order by p.Product_ID)
    from Sales.Sales_Order_Header as h
             join Sales.Sales_Order_Detail as d on d.Sales_Order_ID = h.Sales_Order_ID
             join Production.Product as p on p.Product_ID = d.Product_ID
             join Production.Product_Subcategory as ps on ps.Product_Subcategory_ID = p.Product_Subcategory_ID
), calculatedResults as (
    select distinct pcc.Customer_ID, pcc.Category_ID,
                    cast(max(pcc.products) over (partition by pcc.Customer_ID, pcc.Category_ID) as float) as max_products,
                    (pic.products - max(pcc.products) over (partition by pcc.Customer_ID, pcc.Category_ID)) as diff_products,
                    pic.products
    from prodsPerCustCategory as pcc
             join prodsInCategory as pic on pic.Category_ID = pcc.Category_ID
)
select Customer_ID, Category_ID,
       max_products / diff_products as result
from calculatedResults
where diff_products <> 0
order by Customer_ID;

```
12. Вывести для продуктов их стоимость, скидку, стоимость со скидкой, процент скидки для тех у которых количество продаж больше 5  
```SQL
with t AS (
    SELECT DISTINCT
        pp.product_id,
        sod.unit_price AS original_price,
        sod.unit_price_discount AS discount_amount,
        (sod.unit_price - (sod.unit_price_discount * sod.unit_price)) AS price_with_discount,
        (sod.unit_price - (sod.unit_price - (sod.unit_price_discount * sod.unit_price))) AS total_discount,
        SUM(sod.order_qty) OVER (PARTITION BY pp.product_id) AS total_sales
    FROM
        production.product pp
            JOIN
        sales.sales_order_detail sod ON pp.product_id = sod.product_id
)
SELECT
    DISTINCT
    product_id,
    original_price,
    discount_amount,
    price_with_discount,
    total_discount,
    total_sales
FROM t
WHERE total_sales > 5
```
13. Вывести продукт его цену и динамику изменения его цены  
```SQL
WITH RankedPrices AS (
    SELECT
        p.product_id,
        p.name AS product_name,
        sod.unit_price,
        soh.order_date,
        ROW_NUMBER() OVER (PARTITION BY p.product_id ORDER BY soh.order_date) AS price_rank
    FROM
        production.product AS p
            INNER JOIN
        sales.sales_order_detail AS sod
        ON p.product_id = sod.product_id
            INNER JOIN
        sales.sales_order_header AS soh
        ON sod.sales_order_id = soh.sales_order_id
),
     PriceTrends AS (
         SELECT
             curr.product_id,
             curr.product_name,
             curr.unit_price AS current_price,
             curr.order_date AS current_date,
    curr.unit_price - prev.unit_price AS price_change
FROM
    RankedPrices AS curr
    LEFT JOIN
    RankedPrices AS prev
ON curr.product_id = prev.product_id
    AND curr.price_rank = prev.price_rank + 1
    )
SELECT
    product_id,
    product_name,
    current_date,
    current_price,
    price_change
FROM
    PriceTrends
ORDER BY
    product_id,
    current_date;
```
14. Вывести на экран для каждого продукта номер продукта, номер чека, дату продажи, стоимость продажи и динамику цены товара по сравнению с предыдущим чеком, в котором он был продан  
```SQL
WITH RankedSales AS (
    SELECT
        p.product_id,
        soh.sales_order_id,
        soh.order_date,
        sod.unit_price,
        ROW_NUMBER() OVER (PARTITION BY p.product_id ORDER BY soh.order_date) AS sale_rank
    FROM
        production.product AS p
            INNER JOIN
        sales.sales_order_detail AS sod
        ON p.product_id = sod.product_id
            INNER JOIN
        sales.sales_order_header AS soh
        ON sod.sales_order_id = soh.sales_order_id
),
     PriceComparison AS (
         SELECT
             curr.product_id,
             curr.sales_order_id,
             curr.order_date,
             curr.unit_price AS current_price,
             curr.unit_price - prev.unit_price AS price_difference
         FROM
             RankedSales AS curr
                 LEFT JOIN
             RankedSales AS prev
             ON curr.product_id = prev.product_id
                 AND curr.sale_rank = prev.sale_rank + 1
     )
SELECT
    pc.product_id,
    pc.sales_order_id,
    pc.order_date,
    pc.current_price AS sale_price,
    pc.price_difference AS price_change
FROM
    PriceComparison AS pc
ORDER BY
    pc.product_id,
    pc.order_date;
```
15. Найти для каждого чека его номер, стоимость всех товаров в чеке без скидки, стоимость всех товаров со скидкой, при чем выбрать такие чеки сумма скидки в которых не меньше 1000, а процент скидки не больше 5  
```SQL
WITH CheckTotals AS (
    SELECT
        soh.sales_order_id,
        SUM(sod.unit_price * sod.order_qty) OVER (PARTITION BY soh.sales_order_id) AS total_without_discount,
        SUM((sod.unit_price * sod.order_qty) * (1 - sod.unit_price_discount)) OVER (PARTITION BY soh.sales_order_id) AS total_with_discount,
        SUM((sod.unit_price * sod.order_qty) * sod.unit_price_discount) OVER (PARTITION BY soh.sales_order_id) AS total_discount_amount,
        AVG(sod.unit_price_discount * 100) OVER (PARTITION BY soh.sales_order_id) AS discount_percentage
    FROM
        sales.sales_order_header AS soh
            INNER JOIN
        sales.sales_order_detail AS sod
        ON soh.sales_order_id = sod.sales_order_id
)
SELECT DISTINCT
    sales_order_id,
    total_without_discount,
    total_with_discount
FROM
    CheckTotals
WHERE
    total_discount_amount >= 1000
  AND discount_percentage <= 5
ORDER BY
    sales_order_id;
```