# *ЛР-3* тестовые

1. Найти и вывести на экран название продуктов и название категорий товаров, к которым относится этот продукт, с учетом того, что в выборку попадут только товары с цветом Red и ценой не менее 100.  
```SQL
SELECT p.name, pc.name
FROM production.product AS p JOIN production.product_subcategory AS ps
                                  ON p.product_subcategory_id = ps.product_subcategory_id
                             JOIN production.product_category as pc
                                  ON ps.product_category_id = pc.product_category_id
WHERE color = 'Red' and list_price > 100
```
2. Вывести на экран названия подкатегорий с совпадающими именами.  
```SQL
SELECT ps1.name, ps1.product_subcategory_id, ps2.product_subcategory_id
FROM production.product_subcategory as ps1 JOIN production.product_subcategory as ps2
                                                ON ps1.name = ps2.name
```
3. Вывести на экран название категорий и количество товаров в данной категории.  
```SQL
SELECT pc.name, count(*)
FROM production.product as p JOIN production.product_subcategory as ps
                                  ON p.product_subcategory_id = ps.product_subcategory_id
                             JOIN production.product_category as pc
                                  ON ps.product_category_id = pc.product_category_id
GROUP BY pc.product_category_id
```
4. Вывести на экран название подкатегории, а также количество товаров в данной подкатегории с учетом ситуации, что могут существовать подкатегории с одинаковыми именами.  
```SQL
SELECT ps.name, count(*)
FROM production.product as pr JOIN production.product_subcategory as ps
                                   ON pr.product_subcategory_id = ps.product_subcategory_id
GROUP BY ps.product_subcategory_id
```
5. Вывести на экран название первых трех подкатегорий с небольшим количеством товаров.  
```SQL
SELECT ps.name
FROM production.product as pr JOIN production.product_subcategory as ps
                                   ON pr.product_subcategory_id = ps.product_subcategory_id
GROUP BY ps.product_subcategory_id
ORDER BY count(*) desc
    limit 3
```
6. Вывести на экран название подкатегории и максимальную цену продукта с цветом Red в этой подкатегории.  
```SQL
SELECT ps.name, max(list_price)
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
WHERE color = 'Red'
GROUP BY ps.product_subcategory_id
```
7. Вывести на экран название поставщика и количество товаров, которые он поставляет.  
```SQL
SELECT pv.name, count(*)
FROM purchasing.vendor as pv
         join purchasing.product_vendor prv
              ON pv.business_entity_id = prv.business_entity_id
GROUP BY name
```
8. Вывести на экран название товаров, которые поставляются более чем одним поставщиком.  
```SQL
SELECT pr.name
FROM production.product as pr
         join purchasing.product_vendor prv
              ON pr.product_id = prv.product_id
GROUP BY pr.name
HAVING count(*)>1
```
9. Вывести на экран название самого продаваемого товара.  
```SQL
SELECT pr.name
FROM production.product as pr
         join sales.sales_order_detail as sod
              ON pr.product_id = sod.product_id
GROUP BY pr.name
ORDER BY count(*) desc
    limit 1
```
10. Вывести на экран название категории, товары из которой продаются наиболее активно.  
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
ORDER BY count(*) desc
    limit 1
```
11. Вывести на экран названия категорий, количество подкатегорий и количество товаров в них.  
```SQL
SELECT pc.name, count(distinct ps.product_subcategory_id), count(product_id)
FROM production.product as pr
         join production.product_subcategory as ps
              ON pr.product_subcategory_id = ps.product_subcategory_id
         join production.product_category as pc
              ON ps.product_category_id = pc.product_category_id
GROUP BY pc.name
```
12. Вывести на экран номер кредитного рейтинга и количество товаров, поставляемых компаниями, имеющими этот кредитный рейтинг.  
```SQL
SELECT pv.credit_rating, count(*)
FROM purchasing.vendor as pv
         join purchasing.product_vendor as prv
              ON pv.business_entity_id = prv.business_entity_id
GROUP BY pv.credit_rating
```
13. Найти первых трех поставщиков, отсортированных по количеству поставляемых товаров, с учетом ситуации, что количество поставляемых товаров может совпадать для разных поставщиков.  
```SQL
SELECT pv.name, count(*)
FROM purchasing.product_vendor as prv
         join purchasing.vendor as pv
              ON prv.business_entity_id = pv.business_entity_id
GROUP BY pv.name
ORDER BY count(*) desc
    limit 3
```
14. Найти для каждого поставщика количество подкатегорий продуктов, к которым относится продукты, поставляемые им, без учета ситуации, когда продукт не относится ни к какой подкатегории.  
```SQL
SELECT pv.name, count(distinct pr.product_subcategory_id)
FROM production.product as pr
         join purchasing.product_vendor as prv
              ON pr.product_id = prv.product_id
         join purchasing.vendor as pv
              ON prv.business_entity_id = pv.business_entity_id
WHERE pr.product_subcategory_id is not null
GROUP BY pv.name
ORDER BY count(distinct pr.product_subcategory_id) desc
```
15. Проверить, есть ли продукты с одинаковым названием, если есть, то вывести эти названия. (Решение через JOIN)  
```SQL
SELECT pr1.name
FROM production.product as pr1
         join production.product as pr2
              ON pr1.name = pr2.name and pr1.product_id!=pr2.product_id
WHERE pr1.name is not null
```