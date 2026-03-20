package com.example.library;

import com.example.library.db.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();

        Menu menu = new Menu();
        menu.start();
    }
}