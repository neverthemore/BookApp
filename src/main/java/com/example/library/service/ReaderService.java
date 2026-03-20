package com.example.library.service;

import com.example.library.dao.ReaderDAO;
import com.example.library.model.Reader;

import java.sql.SQLException;
import java.util.List;

public class ReaderService {
    private final ReaderDAO readerDAO = new ReaderDAO();

    public void registerReader(Reader reader) throws SQLException {
        readerDAO.addReader(reader);
    }

    public List<Reader> listAllReaders() throws SQLException {
        return readerDAO.getAllReaders();
    }

    public Reader getReaderById(int id) throws SQLException {
        return readerDAO.getReaderById(id);
    }
}