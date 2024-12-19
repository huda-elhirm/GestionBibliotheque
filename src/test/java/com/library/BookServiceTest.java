package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO();
        bookService = new BookService(bookDAO);
    }

    @Test
    void testAddBook() {
        Book book = new Book(1, "Java Programming", "John Doe","AWS",2021,"978-3-16-148410-0" , true);
        bookService.addBook(book);
        assertEquals(1, bookDAO.getAllBooks().size());
        assertEquals("Java Programming", bookDAO.getBookById(1).get().getTitle());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book(1, "Java Programming", "John Doe","AWS",2021,"978-3-16-148510-0" , true);
        bookService.addBook(book);
        bookService.updateBook(new Book(1, "Advanced Java", "John Doe","PIXAR",2021,"978-3-16-148410-2" , false));
        assertEquals("Advanced Java", bookDAO.getBookById(1).get().getTitle());
        assertFalse(bookDAO.getBookById(1).get().isAvailable());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(1, "Java Programming", "John Doe","AWS",2021,"978-3-16-148510-0" , true);
        bookService.addBook(book);
        bookService.deleteBook(1);
        assertTrue(bookDAO.getBookById(1).isEmpty());
    }
}
