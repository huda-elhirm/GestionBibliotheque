package com.library.dao;

import com.library.model.Borrow;
import com.library.model.Student;
import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BorrowDAO {

    private static final Logger logger = Logger.getLogger(BorrowDAO.class.getName());

    private final StudentDAO studentDAO;
    private final BookDAO bookDAO;

    public BorrowDAO() {
        this.studentDAO = new StudentDAO();
        this.bookDAO = new BookDAO();
    }

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows";

        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Fetch associated student and book
                int studentId = rs.getInt("student_id");
                int bookId = rs.getInt("book_id");

                Student student = studentDAO.getStudentById(studentId).get();
                Book book = bookDAO.getBookById(bookId).get();

                Borrow borrow = new Borrow(
                        rs.getInt("id"),
                        student,
                        book,
                        rs.getDate("borrow_date"),
                        rs.getDate("return_date")
                );
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    public void save(Borrow borrow) {
        String query = "INSERT INTO borrows (id, student_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, borrow.getId());
            // Set student ID
            stmt.setInt(2, borrow.getStudent().getId());

            // Set book ID
            stmt.setInt(3, borrow.getBook().getId());

            // Set borrow date
            if (borrow.getBorrowDate() != null) {
                stmt.setDate(4, new java.sql.Date(borrow.getBorrowDate().getTime()));
            } else {
                stmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            }

            // Set return date
            if (borrow.getReturnDate() != null) {
                stmt.setDate(5, new java.sql.Date(borrow.getReturnDate().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.executeUpdate();

//            // Retrieve the generated ID
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    borrow.setId(generatedKeys.getInt(1));
//                }
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBorrow(Borrow borrow) {
        save(borrow); // Reuse the save method
    }

    public Borrow getBorrowById(int id) {
        String query = "SELECT * FROM borrows WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Fetch associated student and book
                    int studentId = rs.getInt("student_id");
                    int bookId = rs.getInt("book_id");

                    Student student = studentDAO.getStudentById(studentId).get();
                    Book book = bookDAO.getBookById(bookId).get();

                    return new Borrow(
                            rs.getInt("id"),
                            student,
                            book,
                            rs.getDate("borrow_date"),
                            rs.getDate("return_date")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBorrow(Borrow borrow) {
        String query = "UPDATE borrows SET student_id = ?, book_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());

            if (borrow.getBorrowDate() != null) {
                stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (borrow.getReturnDate() != null) {
                stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setInt(5, borrow.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBorrow(int id) {
        String query = "DELETE FROM borrows WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Borrow> getBorrowsByStudent(Student student) {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM borrows WHERE student_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, student.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    Book book = bookDAO.getBookById(bookId).get();

                    Borrow borrow = new Borrow(
                            rs.getInt("id"),
                            student,
                            book,
                            rs.getDate("borrow_date"),
                            rs.getDate("return_date")
                    );
                    borrows.add(borrow);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    public void deleteAllBorrows() {
        String query = "DELETE FROM Borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            logger.severe("Erreur lors de la suppression des emprunts : " + e.getMessage());
        }
    }


}