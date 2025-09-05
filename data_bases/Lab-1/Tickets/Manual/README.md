# *ЛР-1* тестовые

1. Найти и вывести на экран названия продуктов, их цвет и размер.  
```SQL
SELECT name,color,size FROM production.product
```
2. Найти и вывести на экран названия, цвет и размер таких продуктов, у которых цена более 100.  
```SQL
SELECT name,color,size FROM production.product
WHERE listprice > 100
```
3. Найти и вывести на экран название, цвет и размер таких продуктов, у которых цена менее 100 и цвет Black.  
```SQL
SELECT name,color,size FROM production.product
WHERE listprice > 100 AND color='Black'
```
4. Найти и вывести на экран название, цвет и размер таких продуктов, у которых цена менее 100 и цвет Black, упорядочив вывод по возрастанию стоимости продуктов.  
```SQL
SELECT name,color,size FROM production.product
WHERE listprice > 100 AND color='Black'
ORDER BY listprice
```
5. Найти и вывести на экран название и размер первых трех самых дорогих товаров с цветом Black.  
```SQL
SELECT name,size,listprice FROM production.product
WHERE color='Black'
ORDER BY listprice DESC
LIMIT 3
```
6. Найти и вывести на экран название и цвет таких продуктов, для которых определен и цвет, и размер.  
```SQL
SELECT name,color FROM production.product
WHERE color is not null and size is not null
```
7. Найти и вывести на экран не повторяющиеся цвета продуктов, у которых цена находится в диапазоне от 10 до 50 включительно.  
```SQL
SELECT DISTINCT color FROM production.product
WHERE listprice >10 and listprice <=50
```
8. Найти и вывести на экран все цвета таких продуктов, у которых в имени первая буква ‘L’ и третья ‘N’.  
```SQL
SELECT color,name FROM production.product
WHERE name LIKE 'L_N%'
```
9. Найти и вывести на экран названия таких продуктов, которых начинаются либо на букву ‘D’, либо на букву ‘M’, и при этом длина имени – более трех символов.  
```SQL
SELECT name FROM production.product
WHERE (name LIKE 'D%' or name LIKE 'M%') and length(name)>3
```
10. Вывести на экран названия продуктов, у которых дата начала продаж – не позднее 2012 года.  
```SQL
SELECT name FROM production.product
WHERE EXTRACT(YEAR FROM sellstartdate) <=2012
```
ИЛИ
```SQL
SELECT name from production.product
WHERE sellstartdate <='2012-12-31'
```
11. Найти и вывести на экран названия всех подкатегорий товаров.  
```SQL
SELECT name FROM production.product_subcategory
```
12. Найти и вывести на экран названия всех категорий товаров.  
```SQL
SELECT name FROM production.product_category
```
13. Найти и вывести на экран имена всех клиентов из таблицы Person, у которых обращение (Title) указано как «Mr.».  
```SQL
SELECT title FROM person.person
WHERE title = 'Mr.'
```
14. Найти и вывести на экран имена всех клиентов из таблицы Person, для которых не определено обращение (Title).  
```SQL
SELECT title FROM person.person
WHERE title is null
```
15. Получить все названия товаров в системе, в названии которых третий символ – либо буква “s”, либо буква “r”. Решить задачу как минимум двумя способами.  
```SQL
SELECT name FROM production.product
WHERE name like '__s%' or name like '__r%'
```
ИЛИ
```SQL
SELECT name FROM production.product
WHERE SUBSTRING(name,3,1) in ('s','r')
```
ИЛИ
```SQL
SELECT name from production.product
WHERE SUBSTRING(name,3,1) ='s' or SUBSTRING(name,3,1) ='r'
```
ИЛИ
```SQL
SELECT name FROM production.product
WHERE RIGHT(LEFT(name,3),1) in ('s','r')
```
ИЛИ
```SQL
SELECT name FROM production.product
WHERE POSITION('s' IN SUBSTRING(name FROM 3 FOR 1)) = 1
   or POSITION('r' IN SUBSTRING(name FROM 3 FOR 1)) = 1
```
16. Получить все названия товаров в системе, в названии которых ровно 5 символов.  
```SQL
SELECT name FROM production.product
WHERE length(name)=5
```
17. Написать запрос, возвращающий названия товаров, которые были в продаже между мартом 2011 года и мартом 2012 года включительно (необходимо учитывать формат даты)  
```SQL
SELECT sellstartdate,sellenddate FROM production.product
WHERE sellstartdate <= '2012-03-31' and sellenddate >= '2011-03-01'
```
18. Найти максимальную стоимость товара (отпускная цена ListPrice) из тех, которые были произведены, начиная с марта 2011 года.  
```SQL
SELECT listprice FROM production.product
WHERE sellstartdate >= '2011-03-01'
ORDER BY listprice DESC
LIMIT 1
```
ИЛИ
```SQL
SELECT listprice FROM production.product
WHERE EXTRACT(YEAR FROM sellstartdate) >= '2011' and EXTRACT(MONTH FROM sellstartdate)>='03' and EXTRACT(DAY FROM sellstartdate)>='01'
ORDER BY listprice DESC
    LIMIT 1
```