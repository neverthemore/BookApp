package com.example.library.dao;

import com.example.library.model.Book;
import com.example.library.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, available_copies) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getAvailableCopies());
            pstmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setIsbn(rs.getString("isbn"));
                b.setAvailableCopies(rs.getInt("available_copies"));
                books.add(b);
            }
        }
        return books;
    }

    public List<Book> findBooksByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + title + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book();
                    b.setId(rs.getInt("id"));
                    b.setTitle(rs.getString("title"));
                    b.setAuthor(rs.getString("author"));
                    b.setIsbn(rs.getString("isbn"));
                    b.setAvailableCopies(rs.getInt("available_copies"));
                    books.add(b);
                }
            }
        }
        return books;
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book b = new Book();
                    b.setId(rs.getInt("id"));
                    b.setTitle(rs.getString("title"));
                    b.setAuthor(rs.getString("author"));
                    b.setIsbn(rs.getString("isbn"));
                    b.setAvailableCopies(rs.getInt("available_copies"));
                    return b;
                }
            }
        }
        return null;
    }

    public void updateBookAvailableCopies(int bookId, int newAvailable) throws SQLException {
        String sql = "UPDATE books SET available_copies = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newAvailable);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }
}