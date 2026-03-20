package com.example.library.model;

import java.time.LocalDateTime;

public class Reader {
    private int id;
    private String name;
    private String email;
    private LocalDateTime regDate;

    public Reader() {
    }

    public Reader(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Email: %s, Registered: %s",
                id, name, email, regDate != null ? regDate : "N/A");
    }
}