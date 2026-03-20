package com.example.library.model;

import java.time.LocalDateTime;

public class Loan {
    private int id;
    private int bookId;
    private int readerId;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    public Loan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format("Loan ID: %d, Book ID: %d, Reader ID: %d, Loaned: %s, Returned: %s",
                id, bookId, readerId, loanDate, returnDate != null ? returnDate : "Not returned");
    }
}