# *Лабораторная работа №6*

> `OVER`

Предложение OVER определяет секционирование и упорядочение набора строк
до применения соответствующей оконной функции.

Синтаксис:

```SQL
OVER ([ <PARTITION BY столбец> ][ <ORDER BY столбец> ][ <ROW или RANGE столбец> ])
```

- `PARTITION BY`: разделяет результирующий набор запроса на секции
  Оконная функция применяется к каждой секции отдельно, и вычисление начинается заново для каждой секции.

- `ORDER BY`: определяет логический порядок строк в каждой секции результирующего набора

- `ROW` or `RANGE`: ограничивает строки в пределах секции, указывая начальную и конечную точки

- `CURRENT ROW`: указывает, что окно начинается или заканчивается на
  текущей строке при использовании совместно с предложением `ROWS` или что окно заканчивается на текущем значении при использовании с предложением `RANGE`.

- `CURRENT ROW` может быть задана и как начальная, и как конечная точка.

- `BETWEEN <граница рамки окна> AND <граница рамки окна>`:
  используется совместно с предложением `ROWS` или `RANGE` для указания нижней (начальной) или верхней (конечной) граничной точки окна

- `<граница рамки окна> (1)` определяет граничную начальную точку, а  
  `<граница рамки окна> (2)` определяет граничную конечную точку.

- `UNBOUNDED FOLLOWING`: указывает, что окно заканчивается на последней строке секции

- `UNBOUNDED FOLLOWING`: может быть указано только как конечная точка окна.

Пример 1:

```SQL
RANGE BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
```

В данном случае параметр определяет, что окно начинается на текущей
строке и заканчивается на последней строке секции.

Пример 2:

```SQL
ROWS BETWEEN 2 FOLLOWING AND 10 FOLLOWIN
```

В этом примере параметр определяет, что окно начинается на второй строке
после текущей и заканчивается на десятой строке после текущей строки.
Эта спецификация не допускается в предложении RANGE.

Фактически предложение OVER виртуально разбивает выбранные строки на
наборы, окна, определяемые в условии PARTITION BY, упорядочивает эти
строки по столбцам, определенным ORDER BY. В рамках этих наборов, окон,
выполняются те или иные агрегирующие, статистические и иные функции.
Результат выполнения этих функций формирует отдельный столбец с
одинаковыми значениями для каждой строки в наборе, окне. Однако можно
для каждой строки в наборе формировать свое значение функции, для чего
используют параметр ROW или RANGE, который определяет диапазон строк, в
наборе, окне, с которыми будет работать функция.

```SQL
SELECT
  sales_order_id,
  product_id,
  order_qty,
  SUM(order_qty) OVER(PARTITION BY sales_order_id) AS total,
  AVG(order_qty) OVER(PARTITION BY sales_order_id) AS "Avg",
  COUNT(order_qty) OVER(PARTITION BY sales_order_id) AS "Count",
  MIN(order_qty) OVER(PARTITION BY sales_order_id) AS "MIN",
  MAX(order_qty) OVER(PARTITION BY sales_order_id) AS "Max"
FROM sales.sales_order_detail
WHERE sales_order_id IN (43659,43664)
```

Следующий запрос использует приведение типа:

```SQL
SELECT
  sales_order_id,
  product_id,
  order_qty,
  SUM(order_qty) OVER(PARTITION BY sales_order_id) AS total  ,
  CAST(1. * order_qty / SUM(order_qty) OVER(PARTITION BY sales_order_id) * 100 AS DECIMAL(5,2)) AS "Percent BY ProductID"
FROM sales.sales_order_detail
WHERE sales_order_id IN (43659,43664)
```

Можно выполнять функцию не на всем наборе, но на части набора. Часть
определяется в зависимости от строки и ее положения в наборе:

```SQL
SELECT
  business_entity_id,
  territory_id,
  TO_CHAR(sales_ytd, 'FM9999999999') AS sales_ytd,
  EXTRACT(YEAR FROM modified_date) AS sales_year,
  TO_CHAR(SUM(sales_ytd) OVER (PARTITION BY territory_id ORDER BY EXTRACT(YEAR FROM modified_date)
                               ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW), 'FM9999999999') AS cumulative_total
FROM sales.sales_person
WHERE territory_id IS NULL OR territory_id  ]  )
```

### Аналитические функции

Функция FIRST_VALUE возвращает первое значение из упорядоченного набора значений.

```SQL
FIRST_VALUE ( [scalar_expression ] ) OVER ( [ partition_by_clause ]
ORDER_BY_clause [ rows_range_clause ] )
```

scalar_expression может быть столбцом, вложенным запросом.

Получение имени самого дешевого продукта в заданной категории продуктов:

```SQL
SELECT 
  name, 
  list_price,
  FIRST_VALUE(name) OVER (ORDER BY list_price ASC) AS least_expensive
FROM production.product
WHERE product_subcategory_id = 37
```

Функция LAST_VALUE возвращает последнее значение из упорядоченного набора значений.

```SQL
LAST_VALUE ( [ scalar_expression ] ) OVER ( [ partition_BY_clause ]
ORDER_BY_clause rows_range_clause )
```

scalar_expression может быть столбцом, вложенным запросом.

Функция LAG обеспечивает доступ к строке с заданным физическим смещением перед началом текущей строки.

```SQL
LAG (scalar_expression [,offset] [,default]) 
OVER ( [ partition_BY_clause ] ORDER_BY_clause )
```

- scalar_expression – возвращаемое значение основано на указанном смещении.

- offset – количество строк до строки перед текущей строкой, из которой необходимо получить значение.

- default – возвращаемое значение, когда offset находится за пределами секции.

Нахождение квоты для работника за год и предыдущий год.

Квоты менялись несколько раз в год, но для первой установленной в году квоты нет предыдущего значения от 2010 года, их не включают в выборку:

```SQL
SELECT 
  business_entity_id, 
  EXTRACT(YEAR FROM quota_date) AS sales_year,
  sales_quota AS current_quota,
  LAG(sales_quota, 1, 0) OVER (ORDER BY EXTRACT(YEAR FROM quota_date)) AS previous_quota
FROM sales.sales_person_quota_history
WHERE business_entity_id = 275 
  AND EXTRACT(YEAR FROM quota_date) IN (2011, 2012);
```

Функция LEAD – доступ к строке на заданном физическом смещении после текущей строки.

```SQL
LEAD ( scalar_expression [ ,offset ] , [ default ] )
OVER( [ partition_BY_clause ] ORDER_BY_clause )
```

Получение квот продаж для указанного работника за последующие годы:

```SQL
SELECT 
  business_entity_id, 
  EXTRACT(YEAR FROM quota_date) AS sales_year,
  sales_quota AS current_quota,
  LEAD(sales_quota, 1, 0) OVER (ORDER BY EXTRACT(YEAR FROM quota_date)) AS next_quota
FROM sales.sales_person_quota_history
WHERE business_entity_id = 275 
  AND EXTRACT(YEAR FROM quota_date) IN (2011, 2012);
```

Функция NTILE распределяет строки упорядоченной секции в заданное количество групп. Группы нумеруются, начиная с единицы. Для каждой строки функция NTILE возвращает номер группы, которой принадлежит строка.

```SQL
NTILE (integer_expression) OVER ( [ <partition_by_clause> ] < ORDER_BY_clause > )
```

integer_expression – положительное целое выражение, указывающее число групп, на которые необходимо разделить каждую секцию

Функция ROW_NUMBER нумерует выходные данные результирующего набора.

```SQL
ROW_NUMBER ( )
OVER ( [ PARTITION BY value_expressiON , ... [ n ] ] ORDER_BY_clause)
```

Пример использования функций:

```SQL
SELECT
    p.first_name,
    p.last_name,
    ROW_NUMBER() OVER (ORDER BY a.postal_code) AS "Row Number",
    NTILE(4) OVER (ORDER BY a.postal_code) AS quartile,
    s.sales_ytd,
    a.postal_code
FROM
    sales.sales_person AS s
    INNER JOIN person.person AS p
        ON s.business_entity_id = p.business_entity_id
    INNER JOIN person.address AS a
        ON a.address_id = p.business_entity_id
WHERE
    territory_id IS NOT NULL
    AND sales_ytd <> 0;
```
