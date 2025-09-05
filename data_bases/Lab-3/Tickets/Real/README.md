# *ЛР-3* реальные

1. Вывести список поставщиков, название вендоров, отсортированных по количеству поставляемых товаров в порядке возрастания  
```SQL
SELECT pv.name, count(*)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
GROUP BY pv.business_entity_id
ORDER BY count(*)
```
2. Вывести первые 2 категории отсортированные по количеству продуктов по возрастанию  
```SQL
SELECT pc.product_category_id
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.product_category_id
ORDER BY count(*)
    limit 2
```
3. Вывести названия категорий в которых более 20 товаров  
```SQL
SELECT pc.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.product_category_id
HAVING count(*) > 20
```
4. Вывести подкатегории где есть хотя бы один красный товар  
```SQL
SELECT ps.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
WHERE pr.color = 'Red'
GROUP BY ps.product_subcategory_id
HAVING count(*) > 0
```
5. Вывести названия категорий товаров в порядке возрастания по количеству продаж товаров этих категорий  
```SQL
SELECT pc.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
GROUP BY pc.name
ORDER BY count(order_qty)
```
6. Вывести название товаров которые не были проданы ни разу  
```SQL
SELECT pr.name
FROM production.product as pr
         left join sales.sales_order_detail as sod
                   ON pr.product_id = sod.product_id
GROUP BY pr.name
HAVING count(order_qty) = 0
```
7. Вывести название и категорию товара голубого цвета, который купили ровно 2 раза  
```SQL
SELECT pr.name, pc.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
WHERE pr.color = 'Blue'
GROUP BY pr.name, pc.name
HAVING count(order_qty) = 2
```
8. Вывести названия, id поставщиков, количество различных цветов товаров, которые они поставляют, и всё это отсортировать по количеству различных цветов  
```SQL
SELECT pv.name, count(distinct pr.color)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
         join production.product as pr
              ON pr.product_id = prv.product_id
GROUP BY pv.name
ORDER BY count(distinct pr.color)
```
9. Вывести список поставщиков, название вендоров, отсортированных по количеству поставляемых товаров в порядке возрастания  
```SQL
SELECT pv.name, count(pr.product_id)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
         join production.product as pr
              ON pr.product_id = prv.product_id
GROUP BY pv.name
ORDER BY count(pr.product_id)
```
10. Вывести первые 2 категории отсортированные по количеству продуктов по возрастанию  
```SQL
SELECT pc.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.name
ORDER BY count(pr.name)
    limit 2
```
11. Вывести на экран первых три товара с наибольшим количеством продаж  
```SQL
SELECT pr.name
FROM production.product as pr
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
GROUP BY pr.name
ORDER BY count(order_qty) desc
    limit 3
```
12. Найти сколько различных размеров товаров приходится на каждую категорию товаров  
```SQL
SELECT pc.name, count(distinct pr.size)
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.name
```
13. Найти название товаров, которые были куплены или три, или пять раз  
```SQL
SELECT pr.name
FROM production.product as pr
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
GROUP BY pr.name
HAVING count(*) = 3 or count(*) = 5
```
14. Вывести подкатегории, в которых больше 5 товаров  
```SQL
SELECT ps.name
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
GROUP BY ps.name
HAVING count(*) > 5
```