# *ЛР-4* реальные

1. Вывести id покупателей у которых минимум 3 чека и минимум 3 различных продукта в этом чеке  
```SQL
SELECT customer_id
FROM sales.sales_order_header
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    GROUP BY sales_order_id
    HAVING count(distinct product_id) >=3
)
GROUP BY customer_id
HAVING count(sales_order_id)>=3
```
2. Вывести список продуктов, цена которых выше средней цены товаров в подкатегории, к которой относится товар  
```SQL
SELECT name
FROM production.product as pr1
WHERE list_price > (
    SELECT AVG(list_price)
    FROM production.product as pr2
    WHERE pr1.product_subcategory_id
              = pr2.product_subcategory_id
)
```
3. Вывести id продуктов у которых стоимость больше средней стоимости продуктов которые были куплены больше трёх раз когда-то  
```SQL
SELECT pr1.product_id
FROM production.product as pr1
WHERE pr1.list_price > (
    SELECT AVG(pr2.list_price)
    FROM production.product as pr2
    WHERE pr2.product_id IN (
        SELECT product_id
        FROM sales.sales_order_detail
        GROUP BY product_id
        HAVING count(order_qty)>3
    )
)
```
4. Вывести название категории продукта который были проданы больше всего по количеству чеков  
```SQL
SELECT pc.name
FROM production.product_category as pc
WHERE pc.product_category_id = (
    SELECT ps.product_category_id
    FROM production.product_subcategory as ps
    WHERE ps.product_subcategory_id = (
        SELECT pr.product_subcategory_id
        FROM production.product as pr
        WHERE product_id = (
            SELECT sod.product_id
            FROM sales.sales_order_detail as sod
            GROUP BY sod.product_id
            ORDER BY count(sod.sales_order_id) desc
    limit 1
    )
    )
    )
```
5. Найти id покупателей таких чтобы суммарная стоимость всех их заказов была больше средней суммарной стоимости заказов для каждого покупателя  
```SQL
SELECT soh.customer_id
FROM sales.sales_order_header as soh
GROUP BY soh.customer_id
HAVING sum(soh.total_due) > (
    SELECT AVG(summ)
    FROM (
             SELECT sum(soh2.total_due) as summ
             FROM sales.sales_order_header as soh2
             GROUP BY soh2.customer_id
         )
)
```
6. Вывести чеки, где каждый товар стоит больше чем средняя цена всех товаров  
```SQL
SELECT sod1.sales_order_id
FROM sales.sales_order_detail AS sod1
WHERE NOT EXISTS (
    SELECT sod2.product_id
    FROM sales.sales_order_detail AS sod2
    WHERE sod1.sales_order_id = sod2.sales_order_id
      AND unit_price <= (
        SELECT AVG(unit_price)
        FROM sales.sales_order_detail
    )
)
```
7. Найти номера тех покупателей, у которых есть как минимум два чека, и каждый из этих чеков содержит как минимум три товара, каждый из которых как минимум был куплен другими покупателями три раза  
```SQL
SELECT customer_id
FROM sales.sales_order_header as soh
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    WHERE product_id IN (
        SELECT product_id
        FROM sales.sales_order_detail AS sod
        WHERE sales_order_id IN (
            SELECT sales_order_id
            FROM sales.sales_order_header
            WHERE sales_order_id IN (
                SELECT sales_order_id
                FROM sales.sales_order_detail
                GROUP BY sales_order_id
                HAVING COUNT(product_id) >= 3
            )
        )
        GROUP BY product_id
        HAVING COUNT(DISTINCT (
            SELECT soh2.customer_id
            FROM sales.sales_order_header as soh2
            WHERE soh2.sales_order_id = sod.sales_order_id
        )) >= 3
    )
    GROUP BY sales_order_id
    HAVING COUNT(product_id) >= 3
)
GROUP BY customer_id
HAVING COUNT(sales_order_id) >= 2;
```
8. Выведите покупателей который ни разу не покупали один и тот же товар дважды  
```SQL
SELECT distinct customer_id
FROM sales.sales_order_header as soh
WHERE customer_id NOT IN (
    SELECT customer_id
    FROM sales.sales_order_header as soh2
    WHERE sales_order_id IN (
        SELECT distinct sales_order_id
        FROM sales.sales_order_detail as sod
        GROUP BY sales_order_id, product_id
        HAVING count(product_id) >1
    )
)
```
9. Названия категорий в которых хотя бы 3 товара красного цвета  
```SQL
SELECT distinct name
FROM production.product_category as pc
WHERE pc.product_category_id IN (
    SELECT product_category_id
    FROM production.product_subcategory as ps
    WHERE ps.product_subcategory_id IN (
        SELECT pr.product_subcategory_id
        FROM production.product as pr
        WHERE color = 'Red'
        GROUP BY pr.product_subcategory_id
        HAVING count(*) >=3
    )
)
```
10. Вывести чек в котором есть продукт из категории Accessories и его цена больше максимальной средней цены среди подкатегорий  
```SQL
SELECT sales_order_id
FROM sales.sales_order_detail
WHERE product_id IN (
    SELECT product_id
    FROM production.product AS p
    WHERE product_subcategory_id IN (
        SELECT product_subcategory_id FROM production.product_subcategory
        WHERE product_category_id IN (
            SELECT product_category_id FROM production.product_category AS pc
            WHERE pc.name = 'Accessories'
        )
    ) AND list_price > (
        SELECT AVG(p.list_price)
        FROM production.product AS p
        WHERE product_subcategory_id IN (
            SELECT product_subcategory_id
            FROM production.product_subcategory
        )
        GROUP BY product_subcategory_id
        ORDER BY 1 DESC
    LIMIT 1
    )
    )
```
11. Вывести такие чеки, что в них все товары больше 2 штук и их цена больше средней цены всех товаров  
```SQL
SELECT sales_order_id
FROM sales.sales_order_detail
WHERE sales_order_id IN (
    SELECT sales_order_id
    FROM sales.sales_order_detail
    WHERE product_id IN (
        SELECT product_id
        FROM production.product
        WHERE list_price > (SELECT AVG(list_price) FROM production.product)
    )
    GROUP BY sales_order_id
    HAVING COUNT(product_id) >2
)
GROUP BY sales_order_id
HAVING COUNT(DISTINCT product_id) = (
    SELECT COUNT(*)
    FROM sales.sales_order_detail AS sod
    WHERE sod.sales_order_id = sales.sales_order_detail.sales_order_id
      AND sod.product_id IN (
        SELECT product_id
        FROM production.product
        WHERE list_price > (SELECT AVG(list_price) FROM production.product)
    )
)
```
12. Вывести подкатегорию с наибольшим количеством товаров  
```SQL
SELECT name
FROM production.product_subcategory
WHERE product_subcategory_id IN (
    SELECT product_subcategory_id
    FROM production.product
    GROUP BY product_subcategory_id
)
GROUP BY name
ORDER BY count(*) desc
    LIMIT 1
```
13. Вывести клиентов у которых есть хотя бы 3 чека, в которых продукты были куплены хотя бы 3 раза (присутствуют в 3 разных чеках)  
```SQL
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
```
14. Вывести такие категории, у которых подкатегорий с товарами красного цвета больше чем подкатегорий с товарами черного цвета  
```SQL
SELECT p_c."name"
FROM production.product_category AS p_c
WHERE p_c.product_category_id IN (
    SELECT p_sc.product_category_id
    FROM production.product_subcategory AS p_sc
    WHERE p_sc.product_subcategory_id IN (
        SELECT p_p.product_subcategory_id
        FROM production.product AS p_p
        WHERE p_p.color = 'Red'
        GROUP BY p_p.product_subcategory_id
        HAVING COUNT(*) > (
            SELECT COUNT(*)
            FROM production.product AS p_p2
            WHERE p_p2.color = 'Black'
              AND p_p2.product_subcategory_id = p_p.product_subcategory_id
        )
    )
)
```