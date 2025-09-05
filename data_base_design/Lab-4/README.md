# Лабораторная работа №4. *Маскировка и анонимизация данных*

### **Задачи**

1. Замаскировать поля с **конфиденциальными данными**.
2. Провести **анонимизацию данных**.

---

### Порядок выполнения работы

1. Установите расширение [PostgreSQL Anonymizer](https://postgresql-anonymizer.readthedocs.io/en/stable/INSTALL/).
2. Выберите **поля**, которые необходимо замаскировать, и **модифицируйте** уже существующие таблицы или представления.
    - [Динамическая маскировка данных](https://postgresql-anonymizer.readthedocs.io/en/stable/dynamic_masking/)
3. Выберите данные, которые можно анализировать, скрыв или обобщив конфиденциальные данные. Создайте **три** [Materialized Views](https://www.postgresql.org/docs/current/rules-materializedviews.html), используя:
    1) [Generalization](https://postgresql-anonymizer.readthedocs.io/en/stable/generalization) – заменяет данные более широкими и менее точными значениями, диапазонами.
    2) Используйте **две** [стратегии анонимизации](https://postgresql-anonymizer.readthedocs.io/en/stable/masking_functions/) из списка:
        - Destruction
        - Adding Noise
        - Randomization
        - Faking
        - Advanced Faking
        - Pseudonymization
        - Generic Hashing
        - Partial scrambling
4. Предоставить **отчёт**, включить в него следующие данные:
    - перечень **таблиц и полей**, задействованных в *Materialized Views*;
    - **данные** из *Materialized Views*;
    - **код** по маскировке и анонимизации.

---

### На защите лабораторной необходимо будет продемонстрировать

- как работает **маскировка** на ваших данных
- созданные **Materialized Views**
- сами **данные из БД**
- **код**  
