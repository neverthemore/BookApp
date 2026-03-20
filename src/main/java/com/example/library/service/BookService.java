package com.example.library.service;

import com.example.library.dao.BookDAO;
import com.example.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO = new BookDAO();

    public void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }

    public List<Book> listAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }

    public List<Book> searchBooksByTitle(String title) throws SQLException {
        return bookDAO.findBooksByTitle(title);
    }
}