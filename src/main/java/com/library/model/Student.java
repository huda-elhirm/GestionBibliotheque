package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    // Getters et Setters
    private int id;
    private String name;
    private String email;

    // Constructeur par défaut
    public Student() {
    }

    // Constructeur complet
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public Student(int i, String name) {
        this.id = i;
        this.name = name;
    }

    public Student(String Name) {
        this.name = Name;
    }

    // Additional methods
    public boolean isEmpty() {
        return name == null || name.trim().isEmpty();
    }
}
