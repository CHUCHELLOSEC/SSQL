package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }
    public Integer getTotalCountOfStudents() {
        return studentRepository.getTotalCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        Double average = studentRepository.getAverageAgeOfStudents();
        // Если студентов нет, AVG вернёт null — заменяем на 0.0
        return average != null ? average : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }
}