package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BorrowService {
    private static final Logger LOGGER = Logger.getLogger(BorrowService.class.getName());

    private BookService bookService;
    private StudentService studentService;
    private BorrowDAO borrowDAO;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;

    // Constructor
    public BorrowService(BookService bookService, StudentService studentService, BorrowDAO borrowDAO) {
        this.bookService = bookService;
        this.studentService = studentService;
        this.borrowDAO = borrowDAO;
    }


    // Borrow a book
    public String borrowBook(int studentId, int bookId) {
        try {
            // Retrieve student and book
            Student student = studentDAO.getStudentById(studentId);
            Optional<Book> bookOptional = bookDAO.getBookById(bookId);

            // Validate student and book
            if (student == null) {
                throw new IllegalArgumentException("Student not found");
            }
            if (!bookOptional.isPresent()) {
                throw new IllegalArgumentException("Book not found");
            }

            Book book = bookOptional.get();

            // Check if book is available
            if (!book.isAvailable()) {
                throw new IllegalStateException("Book is not available");
            }

            // Create new borrow record
            Borrow borrow = new Borrow();
            borrow.setStudent(student);
            borrow.setBook(book);
            borrow.setBorrowDate(new Date());

            // Save borrow
            borrowDAO.save(borrow);

            // Update book availability
            book.setAvailable(false);
            bookDAO.update(book);

            return "Livre emprunté avec succès!";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error borrowing book", e);
            throw new RuntimeException("Failed to borrow book", e);
        }
    }
    // Return a book
    public String returnBook(int studentId, int bookId) {
        try {
            // Find active borrow for this student and book
            List<Borrow> studentBorrows = borrowDAO.getBorrowsByStudent(studentDAO.getStudentById(studentId));

            Borrow activeBorrow = studentBorrows.stream()
                    .filter(b -> b.getBook().getId() == bookId && b.getReturnDate() == null)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No active borrow found"));

            // Set return date
            activeBorrow.setReturnDate(new Date());

            // Update borrow record
            borrowDAO.updateBorrow(activeBorrow);

            // Retrieve and update book availability
            Optional<Book> bookOptional = bookDAO.getBookById(bookId);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setAvailable(true);
                bookDAO.update(book);
            }

            return "Livre retourné avec succès!";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error returning book", e);
            throw new RuntimeException("Failed to return book", e);
        }
    }
    // Check if a book is currently borrowed
    public boolean isBookCurrentlyBorrowed(int bookId) {
        List<Borrow> borrows = borrowDAO.getAllBorrows();
        return borrows.stream()
                .anyMatch(borrow ->
                        borrow.getBook().getId() == bookId &&
                                borrow.getReturnDate() == null
                );
    }

    // Get all borrows for a student
    public List<Borrow> getBorrowsByStudent(int studentId) {
        // Validate student exists
        Student student = studentService.findStudentById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found");
        }

        List<Borrow> allBorrows = borrowDAO.getAllBorrows();
        return allBorrows.stream()
                .filter(borrow -> borrow.getStudent().getId() == studentId)
                .collect(Collectors.toList());
    }

    // Get currently borrowed books by student
    public List<Borrow> getCurrentBorrowsByStudent(int studentId) {
        List<Borrow> studentBorrows = getBorrowsByStudent(studentId);
        return studentBorrows.stream()
                .filter(borrow -> borrow.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    // Find overdue borrows
    public List<Borrow> findOverdueBorrows() {
        Date currentDate = new Date();
        List<Borrow> allBorrows = borrowDAO.getAllBorrows();

        return allBorrows.stream()
                .filter(borrow -> borrow.getReturnDate() == null) // Not returned
                .filter(borrow -> isOverdue(borrow, currentDate))
                .collect(Collectors.toList());
    }

    // Check if a borrow is overdue (e.g., more than 14 days)
    private boolean isOverdue(Borrow borrow, Date currentDate) {
        long borrowDuration = currentDate.getTime() - borrow.getBorrowDate().getTime();
        long daysDifference = borrowDuration / (24 * 60 * 60 * 1000);
        return daysDifference > 14;
    }

    // Display borrows
    public void displayBorrows() {
        List<Borrow> borrows = borrowDAO.getAllBorrows();

        if (borrows.isEmpty()) {
            System.out.println("No borrows found.");
            return;
        }

        System.out.println("Borrow List:");
        borrows.forEach(borrow -> {
            System.out.println(String.format(
                    "Borrow ID: %d, Student: %s, Book: %s, Borrow Date: %s, Return Date: %s",
                    borrow.getId(),
                    borrow.getStudent().getName(),
                    borrow.getBook().getTitle(),
                    borrow.getBorrowDate(),
                    borrow.getReturnDate() != null ? borrow.getReturnDate() : "Not returned"
            ));
        });
    }

    // Count of current borrows for a student
    public int getCurrentBorrowCount(int studentId) {
        return getCurrentBorrowsByStudent(studentId).size();
    }

    // Example method to check if student can borrow more books
    public boolean canStudentBorrowMore(int studentId) {
        int maxBorrows = 3; // Example limit
        return getCurrentBorrowCount(studentId) < maxBorrows;
    }
}