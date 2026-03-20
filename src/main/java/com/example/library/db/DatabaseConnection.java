package com.example.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:library.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createBooks = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT UNIQUE NOT NULL,
                available_copies INTEGER NOT NULL DEFAULT 1
            );
        """;
        String createReaders = """
            CREATE TABLE IF NOT EXISTS readers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                reg_date DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;
        String createLoans = """
            CREATE TABLE IF NOT EXISTS loans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id INTEGER NOT NULL,
                reader_id INTEGER NOT NULL,
                loan_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                return_date DATETIME,
                FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE RESTRICT,
                FOREIGN KEY (reader_id) REFERENCES readers(id) ON DELETE RESTRICT
            );
        """;
        String createIndexTitle = "CREATE INDEX IF NOT EXISTS idx_books_title ON books(title);";
        String createIndexLoanBook = "CREATE INDEX IF NOT EXISTS idx_loans_book_id ON loans(book_id);";
        String createIndexLoanReader = "CREATE INDEX IF NOT EXISTS idx_loans_reader_id ON loans(reader_id);";
        String createIndexLoanReturn = "CREATE INDEX IF NOT EXISTS idx_loans_return_date ON loans(return_date);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createReaders);
            stmt.execute(createLoans);
            stmt.execute(createIndexTitle);
            stmt.execute(createIndexLoanBook);
            stmt.execute(createIndexLoanReader);
            stmt.execute(createIndexLoanReturn);
            System.out.println("База данных инициализирована успешно.");
        } catch (SQLException e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }
}