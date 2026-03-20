package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Reader;
import com.example.library.service.BookService;
import com.example.library.service.ReaderService;
import com.example.library.service.LoanService;
import com.example.library.model.Loan;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final BookService bookService = new BookService();
    private final ReaderService readerService = new ReaderService();
    private final LoanService loanService = new LoanService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    bookMenu();
                    break;
                case "2":
                    readerMenu();
                    break;
                case "3":
                    lendingMenu();
                    break;
                case "4":
                    statisticsMenu();
                    break;
                case "5":
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== Библиотечная система ===");
        System.out.println("1. Работа с книгами");
        System.out.println("2. Работа с читателями");
        System.out.println("3. Операции выдачи");
        System.out.println("4. Статистика");
        System.out.println("5. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void bookMenu() {
        while (true) {
            System.out.println("\n--- Книги ---");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Список всех книг");
            System.out.println("3. Найти книгу по названию");
            System.out.println("4. Назад");
            System.out.print("Выберите: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        addBook();
                        break;
                    case "2":
                        listAllBooks();
                        break;
                    case "3":
                        searchBookByTitle();
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД: " + e.getMessage());
            }
        }
    }

    private void addBook() throws SQLException {
        System.out.print("Название: ");
        String title = scanner.nextLine();
        System.out.print("Автор: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Количество доступных копий: ");
        int copies = Integer.parseInt(scanner.nextLine());
        Book book = new Book(title, author, isbn, copies);
        bookService.addBook(book);
        System.out.println("Книга добавлена.");
    }

    private void listAllBooks() throws SQLException {
        List<Book> books = bookService.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("Книг нет.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void searchBookByTitle() throws SQLException {
        System.out.print("Введите название (или часть): ");
        String title = scanner.nextLine();
        List<Book> books = bookService.searchBooksByTitle(title);
        if (books.isEmpty()) {
            System.out.println("Книги не найдены.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void readerMenu() {
        while (true) {
            System.out.println("\n--- Читатели ---");
            System.out.println("1. Зарегистрировать читателя");
            System.out.println("2. Список всех читателей");
            System.out.println("3. Назад");
            System.out.print("Выберите: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        registerReader();
                        break;
                    case "2":
                        listAllReaders();
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД: " + e.getMessage());
            }
        }
    }

    private void registerReader() throws SQLException {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Reader reader = new Reader(name, email);
        readerService.registerReader(reader);
        System.out.println("Читатель зарегистрирован.");
    }

    private void listAllReaders() throws SQLException {
        List<Reader> readers = readerService.listAllReaders();
        if (readers.isEmpty()) {
            System.out.println("Читателей нет.");
        } else {
            readers.forEach(System.out::println);
        }
    }

    private void lendingMenu() {
        while (true) {
            System.out.println("\n--- Выдача книг ---");
            System.out.println("1. Выдать книгу читателю");
            System.out.println("2. Вернуть книгу");
            System.out.println("3. Книги, выданные читателю");
            System.out.println("4. Назад");
            System.out.print("Выберите: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        issueBook();
                        break;
                    case "2":
                        returnBook();
                        break;
                    case "3":
                        booksIssuedToReader();
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД: " + e.getMessage());
            }
        }
    }

    private void issueBook() throws SQLException {
        System.out.print("ID книги: ");
        int bookId = Integer.parseInt(scanner.nextLine());
        System.out.print("ID читателя: ");
        int readerId = Integer.parseInt(scanner.nextLine());
        loanService.issueBook(bookId, readerId);
        System.out.println("Книга выдана.");
    }

    private void returnBook() throws SQLException {
        System.out.print("ID выдачи: ");
        int loanId = Integer.parseInt(scanner.nextLine());
        loanService.returnBook(loanId);
        System.out.println("Книга возвращена.");
    }

    private void booksIssuedToReader() throws SQLException {
        System.out.print("ID читателя: ");
        int readerId = Integer.parseInt(scanner.nextLine());
        List<Loan> loans = loanService.getLoansByReader(readerId);
        if (loans.isEmpty()) {
            System.out.println("У этого читателя нет выдач.");
        } else {
            loans.forEach(System.out::println);
        }
    }

    private void statisticsMenu() {
        while (true) {
            System.out.println("\n--- Статистика ---");
            System.out.println("1. Популярные книги");
            System.out.println("2. Список выданных книг");
            System.out.println("3. Назад");
            System.out.print("Выберите: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        showPopularBooks();
                        break;
                    case "2":
                        showActiveLoans();
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка БД: " + e.getMessage());
            }
        }
    }

    private void showPopularBooks() throws SQLException {
        List<Object[]> popular = loanService.getPopularBooks(5);
        if (popular.isEmpty()) {
            System.out.println("Нет данных о выдаче книг.");
        } else {
            System.out.println("Популярные книги (топ-5 по количеству выдач):");
            for (Object[] row : popular) {
                System.out.printf("ID: %d, %s - %s (выдач: %d)%n",
                        row[0], row[1], row[2], row[3]);
            }
        }
    }

    private void showActiveLoans() throws SQLException {
        List<Loan> active = loanService.getActiveLoans();
        if (active.isEmpty()) {
            System.out.println("Нет выданных книг.");
        } else {
            System.out.println("Выданные книги (не возвращены):");
            active.forEach(System.out::println);
        }
    }
}