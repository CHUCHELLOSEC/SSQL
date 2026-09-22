package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Существующий метод из урока 3.4 — поиск по возрасту
    List<Student> findByAgeBetween(int min, int max);

    // НОВЫЙ МЕТОД 1: количество всех студентов
    // JPQL-запрос (работает с классом Student, а не с таблицей)
    @Query("SELECT COUNT(s) FROM Student s")
    Integer getTotalCountOfStudents();

    // НОВЫЙ МЕТОД 2: средний возраст всех студентов
    // AVG вернёт null, если нет ни одного студента — это мы обработаем в сервисе
    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAgeOfStudents();

    // НОВЫЙ МЕТОД 3: пять последних студентов
    // nativeQuery = true означает, что это чистый SQL, а не JPQL
    // В SQL имя таблицы — student (как в @Entity), а не Student
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudents();
}