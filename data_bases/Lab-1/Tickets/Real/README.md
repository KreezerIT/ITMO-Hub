# *ЛР-1* реальные

1. Вывести названия продуктов в которых ровно 2 тире.  
```SQL
SELECT name FROM production.product
WHERE LENGTH(name) - LENGTH(REPLACE(name,'-',''))=2
```
2. Вывести middlename людей из person в которых хотя бы 2 буквы (не символа).  
```SQL
SELECT middlename FROM person.person
WHERE middlename ~ '[A-Za-zА-Яа-я].*[A-Za-zА-Яа-я]'
```
ИЛИ
```SQL
SELECT middlename FROM person.person
WHERE middlename similar to '%[A-Za-zА-Яа-я]%[A-Za-zА-Яа-я]%'
```
3. Вывести имена подкатегорий товаров, у которых номера равны 1,3,5  
```SQL
SELECT name FROM production.product_subcategory
WHERE productsubcategoryid in (1,3,5)
```
4. Найти и вывести на экран названия продуктов, для которых определен цвет, но не определен размер  
```SQL
SELECT name FROM production.product
WHERE color is not null and size is null
```