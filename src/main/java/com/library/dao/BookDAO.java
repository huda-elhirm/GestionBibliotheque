package com.library.dao;

import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;



public class BookDAO {

    private static final Logger LOGGER = Logger.getLogger(BookDAO.class.getName());


    // Ajouter un nouveau livre dans la base de données
    public void add(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, published_year) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getPublishedYear());
            
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Livre inséré avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    // Récupérer un livre par son ISBN
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        Book book = null;
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setString(1, isbn);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                book = new Book(1, "Java Programming", "John Doe", true);
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublishedYear(resultSet.getInt("published_year"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
        }
        
        return book;
    }
    
    // Récupérer tous les livres
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection connection = DbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
             
            while (resultSet.next()) {
                Book book = new Book(1, "Java Programming", "John Doe", true);
                book.setId(resultSet.getInt("id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublishedYear(resultSet.getInt("published_year"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        
        return books;
    }

    public void delete(int id) {

        String query = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du livre", e);
        }

    }

    public void update(Book book) {
        String query = "UPDATE book SET title = ?, author = ?, publisher = ?, publishedYear = ?, isbn = ?, available = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getPublishedYear());
            statement.setString(5, book.getIsbn());
            statement.setBoolean(6, book.isAvailable());
            statement.setInt(7, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating book with ID: " + book.getId(), e);
        }
    }

    public Optional<Book> getBookById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book =new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("publishedYear"),
                            rs.getString("isbn")
                    );
                    // Assuming you have an availability column
                    book.setAvailable(rs.getBoolean("available"));
                    return Optional.of(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find book by title
    public Book findBookByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getInt("publishedYear"),
                        resultSet.getString("isbn")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livre : " + e.getMessage());
        }

        return null;
    }
}
