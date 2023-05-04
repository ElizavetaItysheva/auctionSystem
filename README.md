## Auction system

____
### Разработчик
*Итышева Елизавета*

____
## Описание проекта и его функциональность

Система позволяет создавать лоты и делать на них ставки. Ставки делаются путем отправки имени ставящего в POST запросе.

____
## Реализованы следующие функции:

* Создание нового лота
* Запуск торгов по лоту 
* Закрытие торгов по лоту
* Создание ставки по лоту
* Получение полной информации о лоте
* Получение имени первого ставившего на лот
* Получение имени ставившего на лот наибольшее количество раз
* Получение всех лотов постранично
* Экспорт всех лотов в файл CSV

___
## Запуск приложения
* Для запуска приложения необходимо выполнить несколько шагов:
  - Клонировать проект и открыть его в среде разработки (например *IntelliJ IDEA*);
  - В файле **application.properties** указать путь к Вашей базе данных;
  - Запустить метод **main** в файле **AuctionSystemApplication.java**.

После выполнения всех шагов, приложение будет доступно через swagger.
Swagger будет доступен по адресу: http://localhost:8080/swagger-ui/index.html

___
## Стэк технологий
* **Backend**:
    - Java 11
    - Maven
    - Spring Boot
    - Spring Web
    - Spring Data JPA
    - Stream API
    - REST
    - GIT
    - Swagger
* **SQL**:
    - PostgreSQL
