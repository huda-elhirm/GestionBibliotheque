package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;
    private final BorrowDAO borrowDAO=new BorrowDAO();


    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO();
        bookService = new BookService(bookDAO);
        borrowDAO.deleteAllBorrows();
        cleanDatabase();

    }

    private void cleanDatabase() {
        // Supprimer tous les livres de la base de données avant chaque test
        bookDAO.deleteAllBooks();  // Implémentez cette méthode pour supprimer tous les livres.
    }

    @Test
    void testAddBook() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        assertEquals(1, bookDAO.getAllBooks().size());
        assertEquals("Java Programming", bookDAO.getBookById(1).get().getTitle());
    }

    @Test
    void testUpdateBook() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148510-0", true));
        bookService.updateBook(new Book(1, "Advanced Java", "John Doe", "PIXAR", 2021, "978-3-16-148410-2", false));
        assertEquals("Advanced Java", bookDAO.getBookById(1).get().getTitle());
        assertFalse(bookDAO.getBookById(1).get().isAvailable());
    }

    @Test
    void testDeleteBook() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148510-0", true));
        bookService.deleteBook(1);
        assertTrue(bookDAO.getBookById(1).isEmpty());
    }
}