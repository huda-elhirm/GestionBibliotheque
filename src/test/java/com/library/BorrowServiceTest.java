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
        borrowService = new BorrowService(bookService, studentService,borrowDAO);

        // Ajouter un étudiant
        studentDAO.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentDAO.addStudent(new Student(2, "Bob", "bob@example.com"));

        // Ajouter des livres
        bookDAO.add(new Book(1, "Java Programming", "John Doe", true));
        bookDAO.add(new Book(2, "Advanced Java", "Jane Doe", true));
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
        assertEquals("Étudiant ou livre non trouvé.", borrowService.borrowBook(3, 1));
    }
}
