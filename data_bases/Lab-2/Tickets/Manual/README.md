# *ЛР-2* тестовые

1. Найти и вывести на экран количество товаров каждого цвета, исключив из поиска товары, цена которых меньше 30.  
```SQL
SELECT COUNT(*) AS amount FROM production.product
WHERE list_price >= 30
GROUP BY color
```
2. Найти и вывести на экран список, состоящий из цветов товаров, таких, что минимальная цена товара данного цвета более 100.  
```SQL
SELECT color FROM production.product
GROUP BY color
HAVING MIN(list_price) > 100
```
3. Найти и вывести на экран номера подкатегорий товаров и количество товаров в каждой подкатегории.  
```SQL
SELECT product_subcategory_id, count(*) as amount FROM production.product
GROUP BY product_subcategory_id
```
4. Найти и вывести на экран номера товаров и количество фактов продаж данного товара (используется таблица SalesORDERDetail).  
```SQL
SELECT product_id, count(*) as amount FROM sales.sales_order_detail
GROUP BY product_id
```
5. Найти и вывести на экран номера товаров, которые были куплены более пяти раз.  
```SQL
SELECT product_id, sum(order_qty) as zakzai FROM sales.sales_order_detail
GROUP BY product_id
HAVING sum(order_qty) > 5
```
6. Найти и вывести на экран номера покупателей, CustomerID, у которых существует более одного чека, SalesORDERID, с одинаковой датой  
```SQL
SELECT customer_id FROM sales.sales_order_header
GROUP BY customer_id, order_date
HAVING count(sales_order_id) >1
```
7. Найти и вывести на экран все номера чеков, на которые приходится более трех продуктов.  
```SQL
SELECT sales_order_id FROM sales.sales_order_detail
GROUP BY sales_order_id
HAVING count(*)>3
```
8. Найти и вывести на экран все номера продуктов, которые были куплены более трех раз.  
```SQL
SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING sum(order_qty) >3

```
9. Найти и вывести на экран все номера продуктов, которые были куплены или три или пять раз.  
```SQL
SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING count(order_qty) = 3 or count(order_qty) = 5
```
10. Найти и вывести на экран все номера подкатегорий, в которым относится более десяти товаров.  
```SQL
SELECT product_subcategory_id FROM production.product
GROUP BY product_subcategory_id
HAVING count(distinct(product_id))>10
```
11. Найти и вывести на экран номера товаров, которые всегда покупались в одном экземпляре за одну покупку.  
```SQL
SELECT product_id FROM sales.sales_order_detail
GROUP BY product_id
HAVING max(order_qty) = 1
```
12. Найти и вывести на экран номер чека, SalesORDERID, на который приходится с наибольшим разнообразием товаров купленных на этот чек.  
```SQL
SELECT sales_order_id FROM sales.sales_order_detail
GROUP BY sales_order_id
ORDER BY count(distinct(product_id)) DESC
LIMIT 1
```
13. Найти и вывести на экран номер чека, SalesORDERID с наибольшей суммой покупки, исходя из того, что цена товара – это UnitPrice, а количество конкретного товара в чеке – это ORDERQty.  
```SQL
SELECT sales_order_id FROM sales.sales_order_detail
GROUP BY sales_order_id
ORDER BY sum(order_qty*unit_price) DESC
LIMIT 1
```
14. Определить количество товаров в каждой подкатегории, исключая товары, для которых подкатегория не определена, и товары, у которых не определен цвет.  
```SQL
SELECT product_subcategory_id, count(product_id) as amount FROM production.product
WHERE product_subcategory_id is not null and color is not null
GROUP BY product_subcategory_id
```
15. Получить список цветов товаров в порядке убывания количества товаров данного цвета  
```SQL
SELECT color FROM production.product
GROUP BY distinct(color)
ORDER BY count(product_id) DESC
```
16. Вывести на экран ProductID тех товаров, что всегда покупались в количестве более 1 единицы на один чек, при этом таких покупок было более двух.  
```SQL
SELECT product_id FROM sales.sales_order_detail
WHERE order_qty > 1
GROUP BY product_id
HAVING count(order_qty) > 2
```