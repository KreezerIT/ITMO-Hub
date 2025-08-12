# *Знакомство с CI/CD*

### Задача

Добавить в свой проект workflow-файл `main.yml` с помощью механизма **GitHub Actions**, который будет автоматически запускать выполнение unit-тестов, созданных в [предыдущей лабораторной работе](../Lab-4/README.md), при каждом `push` в репозиторий  


### Порядок выполнения

1. Ознакомиться с материалами:
    - [A beginner’s guide to CI/CD and automation on GitHub](https://github.blog/2022-06-03-a-beginners-guide-to-ci-cd-and-automation-on-github/)
    - [Github Actions. Простой пример для уверенного знакомства](https://habr.com/ru/articles/711278/)

2. Создать и настроить workflow-файл для GitHub Actions

### Подготовка отчёта

Отчёт должен содержать:
- сам файл `main.yml` (workflow)
- скриншот с результатами работы workflow в GitHub Actions

---

### Примечание

Авто-тесты должны выполняться на двух встроенных runner'ах:`ubuntu-latest` и `windows-latest`
