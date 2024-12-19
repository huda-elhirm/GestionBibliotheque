package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BookService bookService;
    private StudentService studentService;
    private BorrowDAO borrowDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO();
        studentDAO = new StudentDAO();
        borrowDAO = new BorrowDAO();

        // Initialize BookService and StudentService
        bookService = new BookService(bookDAO);
        studentService = new StudentService(studentDAO);

        // Initialize BorrowService with all dependencies
        borrowService = new BorrowService(bookService, studentService, borrowDAO, bookDAO, studentDAO);

        // Add students
        studentDAO.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentDAO.addStudent(new Student(2, "Bob", "bob@example.com"));

        // Add books
        bookDAO.add(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148510-0", true));
        bookDAO.add(new Book(2, "Advanced Java", "Jane Doe", "PIXAR", 2020, "978-3-16-148510-1", true));
    }

    @Test
    void testBorrowBook() {
        assertEquals("Livre emprunté avec succès!", borrowService.borrowBook(1, 1));
        assertFalse(bookDAO.getBookById(1).get().isAvailable());
    }

    @Test
    void testReturnBook() {
        borrowService.borrowBook(1, 1);
        assertEquals("Livre retourné avec succès!", borrowService.returnBook(1, 1));
        assertTrue(bookDAO.getBookById(1).get().isAvailable());
    }

    @Test
    void testBorrowBookNotAvailable() {
        borrowService.borrowBook(1, 1);
        assertEquals("Le livre n'est pas disponible.", borrowService.borrowBook(2, 1));
    }

    @Test
    void testBorrowBookStudentNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            borrowService.borrowBook(3, 1); // Attempting to borrow with a non-existent student ID
        });
    }
}