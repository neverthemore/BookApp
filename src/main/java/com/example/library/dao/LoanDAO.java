package com.example.library.dao;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.db.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public void issueBook(int bookId, int readerId) throws SQLException {
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            throw new SQLException("Книга недоступна для выдачи");
        }

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            bookDAO.updateBookAvailableCopies(bookId, book.getAvailableCopies() - 1);

            String sql = "INSERT INTO loans (book_id, reader_id, loan_date) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, bookId);
                pstmt.setInt(2, readerId);
                pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void returnBook(int loanId) throws SQLException {
        String selectSql = "SELECT book_id FROM loans WHERE id = ? AND return_date IS NULL";
        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmtSelect = conn.prepareStatement(selectSql)) {
            pstmtSelect.setInt(1, loanId);
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    int bookId = rs.getInt("book_id");

                    String updateLoan = "UPDATE loans SET return_date = ? WHERE id = ?";
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateLoan)) {
                        pstmtUpdate.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                        pstmtUpdate.setInt(2, loanId);
                        pstmtUpdate.executeUpdate();
                    }

                    BookDAO bookDAO = new BookDAO();
                    Book book = bookDAO.getBookById(bookId);
                    if (book != null) {
                        bookDAO.updateBookAvailableCopies(bookId, book.getAvailableCopies() + 1);
                    }
                    conn.commit();
                } else {
                    throw new SQLException("Активная выдача с ID " + loanId + " не найдена");
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<Loan> getLoansByReader(int readerId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE reader_id = ? ORDER BY loan_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, readerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan l = new Loan();
                    l.setId(rs.getInt("id"));
                    l.setBookId(rs.getInt("book_id"));
                    l.setReaderId(rs.getInt("reader_id"));
                    l.setLoanDate(rs.getTimestamp("loan_date").toLocalDateTime());
                    if (rs.getTimestamp("return_date") != null) {
                        l.setReturnDate(rs.getTimestamp("return_date").toLocalDateTime());
                    }
                    loans.add(l);
                }
            }
        }
        return loans;
    }

    public List<Loan> getActiveLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE return_date IS NULL ORDER BY loan_date";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan l = new Loan();
                l.setId(rs.getInt("id"));
                l.setBookId(rs.getInt("book_id"));
                l.setReaderId(rs.getInt("reader_id"));
                l.setLoanDate(rs.getTimestamp("loan_date").toLocalDateTime());
                loans.add(l);
            }
        }
        return loans;
    }

    public List<Object[]> getPopularBooks(int limit) throws SQLException {
        List<Object[]> popular = new ArrayList<>();
        String sql = """
            SELECT b.id, b.title, b.author, COUNT(l.id) as loan_count
            FROM books b
            LEFT JOIN loans l ON b.id = l.book_id
            GROUP BY b.id
            ORDER BY loan_count DESC
            LIMIT ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    popular.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("loan_count")
                    });
                }
            }
        }
        return popular;
    }
}