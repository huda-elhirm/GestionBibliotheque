package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Book {
    // Getters et Setters
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int publishedYear;
    private String isbn;
    private boolean available = true; // Add availability flag

    public Book() {
    }

    // Constructeur complet
    public Book(String title, String author, String publisher, int year) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = year;
    }

    // Constructeur additionnel si nécessaire
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Book(int id, String title, String author, String publisher, int publishedYear, String isbn, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.available = available;
    }

    // Additional methods
    public boolean isEmpty() {
        return title == null || title.trim().isEmpty();
    }

    // Add availability getter and setter
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
