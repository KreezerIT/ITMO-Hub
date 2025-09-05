# *Лабораторная работа №5*

> `CTE`

При написании подзапросов или большого количества JOIN-ов механизм
выбора оптимального алгоритма может работать не оптимально. В таком
случае нам хотелось бы \"дать подсказку\" в выборе порядка соединения.

Решить эту задачу можно несколькими способами:

1. Материализованное представление. Результат выполнения запроса
хранится в БД как и другие таблицы. Из минусов можно отметить доп.
память на хранение и затраты на поддержание актуального состояния.
Данный кейс в лабораторной рассматриваться не будет, так как относится
больше к проектированию.

2. Использование запросов как источников данных или обобщенных
табличных выражений. Данное решение является альтернативным вариантом,
не требующим доп затрат. Использование запроса как источника данных
подразумевает, что пользователь создает запрос, дает ему псевдоним и
использует этот псевдоним аналогично имени таблицы.

Рассмотрим сложный и объемный пример:

Необходимо найти для каждой подкатегории количество товаров, у которых
цена выше средней цены в подкатегории, и количество товаров, у которых
цена ниже средней цены в подкатегории. Оформить вывод в виде трех
столбцов: номер подкатегории, первый показатель, второй показатель.

Решить подобную задачу традиционным способом достаточно сложно, но она
относительно легко решается с использованием псевдонимов для результата
выполнения запроса:

```SQL
SELECT T1.PS, T1.c, T2.c
FROM(
  SELECT COUNT(*) AS c,
    product_subcategory_id AS PS
  FROM production.product AS P
  WHERE list_price = (
      SELECT avg(list_price)
      FROM production.product AS PT
      WHERE pt.product_subcategory_id = p.product_subcategory_id)
      GROUP BY product_subcategory_id
    ) AS T2
ON T1.PS = T2.PS
```

> - в данном примере созданы два независимых запроса: один из которых
> находит номер категории и количество товаров, цена у которых ниже
> средней цены в той же категории, к которой он относится, второй --
> такой же, но знак заменен на «больше или равно»
>
> - каждый запрос получил свой псевдоним, T1 и T2 соответственно, и
> каждому столбцу в запросе дан свой псевдоним
>
> - запрос верхнего уровня использует запросы T1 и T2 точно так же, как
>   обычные таблицы, проводит операцию INNER JOIN и выводит искомый результат

К сожалению, использование такого подхода удобно, когда запрос,
формирующий источник данных, не очень длинный. В противном случае
читаемость кода резко падает, и в этом случае удобнее использовать
обобщенное табличное выражение. Также обобщенное табличное выражение
можно использовать при выполнении однократной операции модификации
данных.

Общий синтаксис:

```SQL
WITH  (столбец 1, столбец 2, … ] AS (запрос)
```

Примечание: в нерекурсивных ОТВ недопустимо использовать операцию
упорядочивания, за исключением случаев использования инструкции LIMIT.

Рассмотрим пример:

Необходимо найти количество чеков, приходящихся на одного покупателя на
каждый год.

При выполнении данных операций пользователь, во-первых, вынужден
выполнять промежуточные действия вручную, во-вторых, актуальность
полученных данных может быть поставлена под сомнение. Использование
подзапросов решит обе эти проблемы:

```SQL
WITH sales_CTE (sales_person_id, sales_order_id, sales_year) AS (
  SELECT sales_person_id,
    sales_order_id,
    DATE_PART('year', order_date) AS sales_year
  FROM sales.sales_order_header
  WHERE sales_person_id IS NOT NULL
)

SELECT sales_person_id,
  COUNT(sales_order_id) AS total_sales,
  sales_year
FROM sales_CTE
GROUP BY sales_year, sales_person_id
ORDER BY sales_person_id, sales_year
```

Примечание: В данном пример определено ОТВ с именем sales_CTE, которое
использовано как источник данных для запроса.

Если необходимо использовать несколько ОТВ в одном запросе, их можно
определить так:

```SQL
WITH
  (столбец 1, столбец 2, … ]
AS
  (запрос),

(столбец 1, столбец 2, … ]
AS
(запрос)
```

### Примеры запросов с решениями:

Вывести номера продуктов, таких, что их цена выше средней цены продукта
в подкатегории, к которой относится продукт. Запрос реализовать двумя
способами. В одном из решений допускается использование обобщенного
табличного выражения:

```SQL
WITH tmp (pscid, acgLP) AS (
  SELECT
    p.product_subcategory_id,
    avg(list_price)
  FROM production.product AS p
  GROUP BY p.product_subcategory_id
)

SELECT p.product_id
FROM production.product AS p
JOIN tmp
ON p.product_subcategory_id = tmp.pscid
WHERE list_price > tmp.acgLP
```

Необходимо найти покупателя, который каждый раз имел разный список
товаров в чеке (по номенклатуре):

```SQL
WITH OrderDetails AS (
    SELECT
        soh.customer_id AS c,
        soh.sales_order_id AS o,
        BIT_XOR(sod.product_id) AS ch
    FROM
        sales.sales_order_detail AS sod
    JOIN
        sales.sales_order_header AS soh ON sod.sales_order_id = soh.sales_order_id
    GROUP BY
        soh.customer_id, soh.sales_order_id
)
SELECT
    tmp.c
FROM
    OrderDetails AS tmp
GROUP BY
    tmp.c
HAVING
    COUNT(tmp.ch) = COUNT(DISTINCT tmp.ch) AND COUNT(tmp.ch) > 1;
```

Найти пары таких покупателей, что список названий товаров, которые они
когда-либо покупали, не пересекается ни в одной позиции:

```SQL
WITH CustomerProducts AS (
    SELECT
        soh.customer_id AS c,
        sod.product_id AS p
    FROM
        sales.sales_order_detail AS sod
    JOIN
        sales.sales_order_header AS soh ON sod.sales_order_id = soh.sales_order_id
)
SELECT
    t1.c, t2.c
FROM
    CustomerProducts AS t1,
    CustomerProducts AS t2
WHERE
    t1.p != ALL (
        SELECT
            sod.product_id AS p
        FROM
            sales.sales_order_detail AS sod
        JOIN
            sales.sales_order_header AS soh ON sod.sales_order_id = soh.sales_order_id
        WHERE
            soh.customer_id = t2.c
    )
LIMIT 3;
```
