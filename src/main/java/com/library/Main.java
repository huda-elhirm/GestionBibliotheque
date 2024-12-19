package com.library;

import com.library.dao.BookDAO;
import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.dao.BorrowDAO;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Création des services
        BookService bookService = new BookService(new BookDAO());
        StudentService studentService = new StudentService();
        BorrowDAO borrowDAO = new BorrowDAO();
        BorrowService borrowService = new BorrowService(bookService,studentService,borrowDAO);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Afficher les emprunts");
            System.out.println("7. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consommer la ligne restante après l'entier

            switch (choice) {
                case 1:
                    System.out.print("Entrez le titre du livre: ");
                    String title = scanner.nextLine();
                    System.out.print("Entrez l'auteur du livre: ");
                    String author = scanner.nextLine();
                    System.out.print("Entrez l'ISBN du livre: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Entrez l'année de publication: ");
                    int publishedYear = scanner.nextInt();

                    Book book = new Book(title, author, isbn, publishedYear);
                    bookService.addBook(book);
                    break;

                case 2:
                    bookService.displayBooks();
                    break;

                case 3:
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String studentName = scanner.nextLine();
                    Student student = new Student(studentName);
                    studentService.addStudent(student);
                    break;

                case 4:
                    studentService.displayStudents();
                    break;

                case 5:
                    // Emprunter un livre
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String borrowStudentName = scanner.nextLine();
                    System.out.print("Entrez le titre du livre: ");
                    String borrowBookTitle = scanner.nextLine();

                    // Rechercher l'étudiant et le livre
                    Student borrowStudent = studentService.findStudentByName(borrowStudentName);
                    Book borrowBook = bookService.findBookByTitle(borrowBookTitle);

                    if (borrowStudent != null && borrowBook != null) {
                        // Créer un emprunt
                        Borrow borrow = new Borrow(
                                0, // ID sera généré automatiquement
                                borrowStudent,
                                borrowBook,
                                new Date(), // Date d'emprunt actuelle
                                null // Pas de date de retour pour l'instant
                        );

                        // Enregistrer l'emprunt
                        borrowService.borrowBook(borrowStudent.getId(),borrowBook.getId());
                    } else {
                        System.out.println("Étudiant ou livre introuvable.");
                    }
                    break;

                case 6:
                    borrowService.displayBorrows();
                    break;

                case 7:
                    running = false;
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }

        scanner.close();
    }
}