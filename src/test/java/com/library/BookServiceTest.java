package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO();
        bookService = new BookService(bookDAO);
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        bookDAO.deleteAllBooks();  // Clear all books from the database before each test
    }

    @Test
    void testAddBook() {
        Book book = new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true);
        bookService.addBook(book);

        List<Book> books = bookDAO.getAllBooks();
        assertEquals(1, books.size(), "There should be one book in the database.");
        assertEquals("Java Programming", books.get(0).getTitle(), "The book title should be 'Java Programming'.");
    }

    @Test
    void testUpdateBook() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        Book updatedBook = new Book(1, "Advanced Java", "John Doe", "PIXAR", 2021, "978-3-16-148410-2", false);
        bookService.updateBook(updatedBook);

        Optional<Book> retrievedBook = bookDAO.getBookById(1);
        assertTrue(retrievedBook.isPresent(), "The updated book should be present.");
        assertEquals("Advanced Java", retrievedBook.get().getTitle(), "The book title should be updated to 'Advanced Java'.");
        assertFalse(retrievedBook.get().isAvailable(), "The book should not be available.");
    }

    @Test
    void testDeleteBook() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        bookService.deleteBook(1);

        assertTrue(bookDAO.getBookById(1).isEmpty(), "The book should be deleted from the database.");
    }

    @Test
    void testGetAllBooks() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        bookService.addBook(new Book(2, "Python Programming", "Jane Doe", "O'Reilly", 2020, "978-1-23-456789-0", true));

        List<Book> books = bookDAO.getAllBooks();
        assertEquals(2, books.size(), "There should be two books in the database.");
        assertEquals("Java Programming", books.get(0).getTitle(), "The first book should be 'Java Programming'.");
        assertEquals("Python Programming", books.get(1).getTitle(), "The second book should be 'Python Programming'.");
    }

    @Test
    void testFindBookById() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        Optional<Book> foundBook = bookService.findBookById(1);

        assertTrue(foundBook.isPresent(), "The book should be found by ID.");
        assertEquals("Java Programming", foundBook.get().getTitle(), "The found book's title should be 'Java Programming'.");
    }

    @Test
    void testFindBookByTitle() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        Book foundBook = bookService.findBookByTitle("Java Programming");

        assertNotNull(foundBook, "The book should be found by title.");
        assertEquals(1, foundBook.getId(), "The found book's ID should be 1.");
    }

    @Test
    void testDeleteAllBooks() {
        bookService.addBook(new Book(1, "Java Programming", "John Doe", "AWS", 2021, "978-3-16-148410-0", true));
        bookService.addBook(new Book(2, "Python Programming", "Jane Doe", "O'Reilly", 2020, "978-1-23-456789-0", true));

        bookDAO.deleteAllBooks();
        assertEquals(0, bookDAO.getAllBooks().size(), "All books should be deleted from the database.");
        System.out.println(bookDAO.getAllBooks().size());
    }
}