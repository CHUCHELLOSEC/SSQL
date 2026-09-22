package ru.hogwarts.school.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/students";
    }

    @AfterEach
    void cleanUp() {
        studentRepository.deleteAll();
    }

    // ---------- 1. POST /students ----------
    @Test
    void shouldCreateStudent() {
        Student student = new Student("Гарри Поттер", 11);

        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гарри Поттер");
        assertThat(response.getBody().getAge()).isEqualTo(11);
    }

    // ---------- 2. GET /students/{id} ----------
    @Test
    void shouldGetStudentById() {
        Student student = new Student("Гермиона Грейнджер", 12);
        Student created = studentRepository.save(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/" + created.getId(),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
    }

    // ---------- 3. GET /students (все) ----------
    @Test
    void shouldGetAllStudents() {
        studentRepository.save(new Student("Гарри", 11));
        studentRepository.save(new Student("Гермиона", 12));
        studentRepository.save(new Student("Рон", 11));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    // ---------- 4. PUT /students ----------
    @Test
    void shouldUpdateStudent() {
        Student student = new Student("Невилл Долгопупс", 9);
        Student created = studentRepository.save(student);
        created.setAge(10);

        HttpEntity<Student> request = new HttpEntity<>(created);
        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAge()).isEqualTo(10);
    }

    // ---------- 5. DELETE /students/{id} ----------
    @Test
    void shouldDeleteStudent() {
        Student student = new Student("Драко Малфой", 11);
        Student created = studentRepository.save(student);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // ---------- 6. GET /students/filter?min=&max= ----------
    @Test
    void shouldGetStudentsByAgeBetween() {
        studentRepository.save(new Student("Гарри", 11));
        studentRepository.save(new Student("Гермиона", 12));
        studentRepository.save(new Student("Рон", 11));
        studentRepository.save(new Student("Драко", 13));

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                baseUrl + "/filter?min=11&max=12",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    // ---------- 7. Негативный кейс: 404 ----------
    @Test
    void shouldReturn404WhenStudentNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/999",
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}