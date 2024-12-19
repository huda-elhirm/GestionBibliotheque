package com.library.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class Borrow {
    // Getters et Setters
    private int id;
    private Student student;
    private Book book;
    private Date borrowDate;
    private Date returnDate;

    // Constructeur complet
    public Borrow(int id, Student student, Book book, Date borrowDate, Date returnDate) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

}
