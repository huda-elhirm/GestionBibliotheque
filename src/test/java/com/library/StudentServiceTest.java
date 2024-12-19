package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAO();
        studentService = new StudentService(studentDAO);
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        studentDAO.deleteAllStudents();
    }

    @Test
    void testAddStudent() {
        Student student = new Student(1, "Alice", "alice@example.com");
        studentService.addStudent(student);

        List<Student> students = studentDAO.getAllStudents();
        assertEquals(1, students.size(), "There should be one student in the database.");
        assertEquals("Alice", students.get(0).getName(), "The student's name should be Alice.");
    }

    @Test
    void testUpdateStudent() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentService.updateStudent(1, "Alice Smith", "alice.smith@example.com");

        Student updatedStudent = studentDAO.getStudentById(1).orElse(null);
        assertNotNull(updatedStudent, "The updated student should not be null.");
        assertEquals("Alice Smith", updatedStudent.getName(), "The student's name should be updated to Alice Smith.");
        assertEquals("alice.smith@example.com", updatedStudent.getEmail(), "The student's email should be updated.");
    }

    @Test
    void testDeleteStudent() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentService.deleteStudent(1);

        assertTrue(studentDAO.getStudentById(1).isEmpty(), "The student should be deleted from the database.");
    }

    @Test
    void testGetAllStudents() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentService.addStudent(new Student(2, "Bob", "bob@example.com"));

        List<Student> students = studentDAO.getAllStudents();
        assertEquals(2, students.size(), "There should be two students in the database.");
        assertEquals("Alice", students.get(0).getName(), "The first student's name should be Alice.");
        assertEquals("Bob", students.get(1).getName(), "The second student's name should be Bob.");
    }

    @Test
    void testFindStudentById() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        Student foundStudent = studentService.findStudentById(1);

        assertNotNull(foundStudent, "The student should be found.");
        assertEquals("Alice", foundStudent.getName(), "The found student's name should be Alice.");
    }

    @Test
    void testFindStudentByName() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        Student foundStudent = studentService.findStudentByName("Alice");

        assertNotNull(foundStudent, "The student should be found by name.");
        assertEquals(1, foundStudent.getId(), "The found student's ID should be 1.");
    }

    @Test
    void testDeleteAllStudents() {
        studentService.addStudent(new Student(1, "Alice", "alice@example.com"));
        studentService.addStudent(new Student(2, "Bob", "bob@example.com"));

        studentDAO.deleteAllStudents();
        assertEquals(0, studentDAO.getAllStudents().size(), "All students should be deleted.");
    }
}