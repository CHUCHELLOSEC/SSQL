package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    // Логгер создаётся один раз и доступен всему классу
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.debug("Was invoked method for create student: name={}, age={}",
                student.getName(), student.getAge());
        return studentRepository.save(student);
    }

    public Optional<Student> getStudent(Long id) {
        logger.debug("Was invoked method for get student by id={}", id);
        return studentRepository.findById(id);
    }

    public List<Student> getAllStudents() {
        logger.debug("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        logger.debug("Was invoked method for update student with id={}", student.getId());

        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        }

        // Ошибка: студента для обновления не существует
        logger.error("No student with id={} to update", student.getId());
        return null;
    }

    public void deleteStudent(Long id) {
        logger.debug("Was invoked method for delete student with id={}", id);
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAgeBetween(int min, int max) {
        logger.debug("Was invoked method for find students by age between {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        logger.debug("Was invoked method for get faculty of student with id={}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public Integer getTotalCountOfStudents() {
        logger.debug("Was invoked method for get total count of students");
        return studentRepository.getTotalCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        logger.debug("Was invoked method for get average age of students");
        Double average = studentRepository.getAverageAgeOfStudents();
        // AVG вернёт null, если нет студентов
        return average != null ? average : 0.0;
    }

    public List<Student> getLastFiveStudents() {
        logger.debug("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }
}