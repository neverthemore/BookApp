
# Библиотечная система (Library Management System)

Консольное приложение на Java для управления библиотекой. Реализованы функции учёта книг, читателей, выдачи и возврата книг, а также статистика. Данные хранятся в реляционной базе данных SQLite. Проект демонстрирует применение JDBC, транзакций, индексов и принципов SOLID.

## 🚀 Функциональные возможности

### Работа с книгами
- Добавление новой книги (название, автор, ISBN, количество копий)
- Просмотр списка всех книг
- Поиск книги по названию (частичное совпадение)

### Работа с читателями
- Регистрация читателя (имя, email)
- Просмотр списка всех читателей

### Операции выдачи
- Выдача книги читателю (автоматическое уменьшение доступных копий)
- Возврат книги (автоматическое увеличение доступных копий)
- Просмотр книг, выданных конкретному читателю

### Статистика
- Топ-5 самых популярных книг (по количеству выдач)
- Список всех выданных книг (не возвращённых)

## 🛠 Технологии

- **Java 25**
- **SQLite** — встраиваемая реляционная БД
- **JDBC** — доступ к базе данных
- **Maven** — управление зависимостями и сборка (опционально)

## 📁 Структура проекта

```
library/
├── pom.xml                          # Конфигурация Maven
├── README.md
├── src/
│   ├── main/
│   │   └── java/com/example/library/
│   │       ├── Main.java            # Точка входа
│   │       ├── Menu.java            # Консольное меню
│   │       ├── db/
│   │       │   └── DatabaseConnection.java  # Подключение к БД
│   │       ├── dao/                 # Data Access Objects
│   │       │   ├── BookDAO.java
│   │       │   ├── ReaderDAO.java
│   │       │   └── LoanDAO.java
│   │       ├── model/               # Модели данных
│   │       │   ├── Book.java
│   │       │   ├── Reader.java
│   │       │   └── Loan.java
│   │       └── service/             # Бизнес-логика
│   │           ├── BookService.java
│   │           ├── ReaderService.java
│   │           └── LoanService.java
└── library.db                       # Файл БД (создаётся автоматически)
```

## ⚙️ Установка и запуск

### Предварительные требования
- Установленная **Java 17+** 
- (Опционально) **Maven 3.6+** для сборки

### Способ 1: Запуск через Maven (рекомендуется)

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/yourusername/library-jdbc-console.git
   cd library-jdbc-console
   ```

2. Соберите проект и запустите:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.example.library.Main"
   ```

### Способ 2: Запуск без Maven (ручная компиляция)

1. Скачайте драйвер SQLite JDBC с [Maven Central](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar) и поместите в корневую папку проекта.

2. Скомпилируйте исходные файлы:
   ```bash
   javac -cp ".;sqlite-jdbc-3.44.1.0.jar" src/main/java/com/example/library/*.java src/main/java/com/example/library/db/*.java src/main/java/com/example/library/dao/*.java src/main/java/com/example/library/model/*.java src/main/java/com/example/library/service/*.java
   ```
   (Для Linux/Mac замените `;` на `:`)

3. Запустите приложение:
   ```bash
   java -cp ".;sqlite-jdbc-3.44.1.0.jar;src/main/java" com.example.library.Main
   ```

### Способ 3: Запуск в IntelliJ IDEA

1. Откройте проект как Maven-проект (файл `pom.xml`).
2. Дождитесь загрузки зависимостей.
3. Нажмите правой кнопкой на файле `Main.java` → **Run 'Main.main()'**.


## 📝 Пример использования

После запуска отображается главное меню:

```
=== Библиотечная система ===
1. Работа с книгами
2. Работа с читателями
3. Операции выдачи
4. Статистика
5. Выход
Выберите пункт:
```

Выберите пункт, следуйте подсказкам. Для выдачи книги потребуются ID книги и читателя (их можно посмотреть в соответствующих списках). Все операции проверяют целостность данных с помощью транзакций.

## 📊 Схема базы данных

```sql
-- Книги
CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT UNIQUE NOT NULL,
    available_copies INTEGER NOT NULL DEFAULT 1
);

-- Читатели
CREATE TABLE readers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Выдачи
CREATE TABLE loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER NOT NULL,
    reader_id INTEGER NOT NULL,
    loan_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    return_date DATETIME,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE RESTRICT,
    FOREIGN KEY (reader_id) REFERENCES readers(id) ON DELETE RESTRICT
);

-- Индексы для ускорения запросов
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_loans_book_id ON loans(book_id);
CREATE INDEX idx_loans_reader_id ON loans(reader_id);
CREATE INDEX idx_loans_return_date ON loans(return_date);
```

## 🏗 Архитектура и принципы

- **SOLID**:
  - **Single Responsibility**: Каждый класс отвечает за свою зону (DAO – работа с БД, Service – бизнес-логика, Menu – взаимодействие с пользователем).
  - **Open/Closed**: Сервисы легко расширяются без изменения существующего кода.
  - **Liskov Substitution**: Использование интерфейсов при необходимости (в текущей реализации подменяемость обеспечивается через наследование в тестах).
  - **Interface Segregation**: Интерфейсы (если бы они были) не перегружены.
  - **Dependency Inversion**: Зависимости абстрагированы через сервисный слой.

- **Транзакции**: Операции выдачи и возврата книги обёрнуты в транзакции, чтобы избежать несогласованности данных.

- **Индексы**: Добавлены на часто используемые поля (`title`, внешние ключи, `return_date`) для повышения производительности запросов.


---

**Примечание:** При первом запуске файл базы данных `library.db` создаётся автоматически в рабочей папке. Для сброса данных достаточно удалить этот файл.
