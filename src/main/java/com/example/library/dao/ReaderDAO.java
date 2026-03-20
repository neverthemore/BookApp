package com.example.library.dao;

import com.example.library.model.Reader;
import com.example.library.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {

    public void addReader(Reader reader) throws SQLException {
        String sql = "INSERT INTO readers (name, email) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reader.getName());
            pstmt.setString(2, reader.getEmail());
            pstmt.executeUpdate();
        }
    }

    public List<Reader> getAllReaders() throws SQLException {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT * FROM readers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reader r = new Reader();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setEmail(rs.getString("email"));
                r.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());
                readers.add(r);
            }
        }
        return readers;
    }

    public Reader getReaderById(int id) throws SQLException {
        String sql = "SELECT * FROM readers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reader r = new Reader();
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    r.setEmail(rs.getString("email"));
                    r.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());
                    return r;
                }
            }
        }
        return null;
    }
}