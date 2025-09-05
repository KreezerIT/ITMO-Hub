# *ЛР-2* реальные

1. Вывести номера подкатегорий, где больше 5 различных продуктов  
```SQL
SELECT product_subcategory_id FROM production.product
GROUP BY product_subcategory_id
HAVING count(distinct(product_id))>5
```
2. Вывести список цветов, у которых в базе есть от 2 до 5 товаров такого цвета  
```SQL
SELECT color FROM production.product
GROUP BY color
HAVING count(*)>=2 and count(*)<=5
```
3. Вывести номера подкатегорий, в которых как минимум 5 товаров цвета Красный  
```SQL
SELECT product_subcategory_id FROM production.product
WHERE color = 'Red'
GROUP BY product_subcategory_id
HAVING count(*)>=5
```
4. Вывести самый популярный товар, который стоит меньше 100  
```SQL
SELECT product_id FROM sales.sales_order_detail
WHERE unit_price < 100
GROUP BY product_id
ORDER BY count(*) desc
LIMIT 1
```
5. Найти все товары, их Product_ID, которые были куплены более чем на три чека  
```SQL
SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING count(distinct(sales_order_id))>3
```
6. Вывести номера продуктов, которые покупали менее 3 раз, но в трех разных чеках  
```SQL
SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING count(order_qty)<3 and count(distinct(sales_order_id))>3
```
