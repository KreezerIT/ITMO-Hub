# Лабораторная работа №2

# Отрабатываемый материал

Изучение языка Java, инструментов сборки и встроенных инструментов для работы с персистентными хранилищами.

# Цель

Изучение архитектурного подхода Сontroller-Service-Dao.
Получение прикладных навыков работы с JDBC (Java DataBase Connectivity).

# Задание

Реализовать приложение для работы с базой данных через ORM.
Необходимо создать модели для описание таблиц баз данных согласно конвенциям JPA.

Дополнительно:
Предусмотреть возможность применения миграций базы данных.

# Функциональные требования

Описать следующие сущности:

- Питомец

Имя  
Дата рождения  
Порода  
Цвет (один из заранее заданных вариантов)  
Хозяин  
Список котиков, с которыми дружит этот котик (из представленных в базе)

- Хозяин (владелец питомца):

Имя  
Дата рождения  
Список котиков

- Реализовать CRUD (create, read, update, delete) операции для описанных вами сущностей.
```java
public T save(T entity);
public void deleteById(long id);
public void deleteByEntity(T entity);
public void deleteAll();
public T update(T entity);
public T getById(long id);
public List<T> getAll();
```

# Не функциональные требования

В качестве базы данных рекомендуется использовать PostgreSQL.  
Для связи с БД рекомендуется использоваться Hibernate.

- Лабораторная работа должна быть выполнена как отдельный модуль/проект.
- Рекомендуется использование JDBC-совместимых конфигураций ORM.
- Использование Spring Framework и Spring Boot - запрещено.
- Сторонние зависимости должны поставляться системой сборки автоматически.

# Unit tests

В качестве тестового фреймворка использовать JUnit.  
Избегать тестирования на настоящей базе, использовать либо готовые mock-системы по типу **TestContainers**, либо **DBUnit**, либо написание заглушек через **Mockito** напрямую.

# Ссылки

https://github.com/codehaus/dbunit/blob/master/core/src/test/java/org/dbunit/BuilderExample.java


### Настройка системы сборки под несколько проектов
- Maven  
  https://www.baeldung.com/maven-multi-module

- Gradle  
  https://docs.gradle.org/current/userguide/intro_multi_project_builds.html

### Hibernate
- tl;dr версия:  
  https://docs.jboss.org/hibernate/orm/6.6/quickstart/html_single/#tutorial_jpa

- Подробная документация:  
  https://docs.jboss.org/hibernate/orm/6.6/userguide/html_single/Hibernate_User_Guide.html#getting-started