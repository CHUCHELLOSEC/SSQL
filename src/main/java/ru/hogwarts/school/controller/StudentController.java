package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.getStudent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updated = studentService.updateStudent(student);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> getStudentsByAgeBetween(
            @RequestParam int min,
            @RequestParam int max) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long id) {
        Faculty faculty = studentService.getFacultyByStudentId(id);
        return faculty != null ? ResponseEntity.ok(faculty) : ResponseEntity.notFound().build();
    }
    // Эндпоинт 1: количество всех студентов
// GET http://localhost:8080/students/count
    @GetMapping("/count")
    public ResponseEntity<Integer> getTotalCountOfStudents() {
        return ResponseEntity.ok(studentService.getTotalCountOfStudents());
    }

    // Эндпоинт 2: средний возраст студентов
// GET http://localhost:8080/students/average-age
    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAgeOfStudents() {
        return ResponseEntity.ok(studentService.getAverageAgeOfStudents());
    }

    // Эндпоинт 3: пять последних студентов
// GET http://localhost:8080/students/last-five
    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }
}