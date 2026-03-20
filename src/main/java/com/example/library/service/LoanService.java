package com.example.library.service;

import com.example.library.dao.LoanDAO;
import com.example.library.model.Loan;

import java.sql.SQLException;
import java.util.List;

public class LoanService {
    private final LoanDAO loanDAO = new LoanDAO();

    public void issueBook(int bookId, int readerId) throws SQLException {
        loanDAO.issueBook(bookId, readerId);
    }

    public void returnBook(int loanId) throws SQLException {
        loanDAO.returnBook(loanId);
    }

    public List<Loan> getLoansByReader(int readerId) throws SQLException {
        return loanDAO.getLoansByReader(readerId);
    }

    public List<Loan> getActiveLoans() throws SQLException {
        return loanDAO.getActiveLoans();
    }

    public List<Object[]> getPopularBooks(int limit) throws SQLException {
        return loanDAO.getPopularBooks(limit);
    }
}