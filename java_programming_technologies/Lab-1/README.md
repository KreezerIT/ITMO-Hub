# Лабораторная работа №1

# Отрабатываемый материал

Изучение языка Java, инструментов сборки и встроенных инструментов для документирования исходного кода.

# Цель

Получение прикладных навыков по настройке окружения для корректной работы с Java-совместимыми проектами.  
Отработка навыков работы с JavaDoc, JUnit, Системами сборки Maven/Gradle.

# Задание

Реализовать систему банкомата, настроить CI.

# Функциональные требования

- создание счета
- просмотр баланса счета
- снятие денег со счета
- пополнение счета
- просмотр истории операций

# Не функциональные требования

- При запуске сборки приложения должна автоматически генерироваться документация JavaDoc.
- Отдельная задача внутри пайплайна системы сборки для запуска интерактивного консольного интерфейса программы.
- При попытке выполнения некорректных операций, методы должны выкидывать проверяемые (checked) исключения бизнес логики.
- Использование Spring Framework и Spring Boot - запрещено.
- Сторонние зависимости должны поставляться системой сборки автоматически.
- Использование репозиториев отличных от Maven Central - запрещено.

# Unit tests

Все методы, выбрасывающие исключения бизнес логики должны быть покрыты тестами.    
В качестве тестового фреймворка использовать JUnit.

# Ссылки

https://learnxinyminutes.com/docs/java/  
https://maven.apache.org/guides/getting-started/  
https://docs.gradle.org/current/userguide/part1_gradle_init.html  
https://github.com/eugenp/tutorials/blob/master/testing-modules/junit-5-basics/src/test/java/com/baeldung/ExceptionUnitTest.java



# Инструкции по настройке CI

## Создание структуры для GitHub Actions

1. В корне репозитория создайте папку `.github/workflows`
2. В этой папке создайте файл `java.yml`

## Добавление CI скрипта

В зависимости от выбранной системы сборки, вам нужно будет вставить в этот файл различный код.

## Maven

```yaml
name: Java CI
on: [push]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Build with Maven
        run: mvn --batch-mode --update-snapshots package
```

## Gradle

```yaml
name: Java CI
on: [push]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Validate Gradle wrapper
        uses: gradle/wrapper-validation-action@v1
      - name: Build with Gradle
        uses: gradle/gradle-build-action@v1
        with:
          arguments: build
```

‼️ В данных скриптах указана версия JDK 21, если вы используете другую версию, то поставьте её в параметре `java-version`

Для решения подобной проблемы  
![image](https://github.com/user-attachments/assets/2715af2f-11fa-45c8-beca-cc28808a0024)
Пользователям Windows необходимо сделать дополнительную манипуляцию над gradle wrapper’ом:
```sh
git update-index --chmod=+x gradlew
```
Далее сделайте `commit`+ `push`, можно напрямую в `master` ветку, без пулл реквеста.