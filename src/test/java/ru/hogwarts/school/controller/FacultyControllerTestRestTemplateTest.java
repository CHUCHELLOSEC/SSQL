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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/faculties";
    }

    @AfterEach
    void cleanUp() {
        facultyRepository.deleteAll();
    }

    // ---------- 1. POST /faculties ----------
    @Test
    void shouldCreateFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Красный");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
        assertThat(response.getBody().getColor()).isEqualTo("Красный");
    }

    // ---------- 2. GET /faculties/{id} ----------
    @Test
    void shouldGetFacultyById() {
        Faculty faculty = new Faculty("Слизерин", "Зелёный");
        Faculty created = facultyRepository.save(faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + created.getId(),
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    // ---------- 3. GET /faculties (все) ----------
    @Test
    void shouldGetAllFaculties() {
        facultyRepository.save(new Faculty("Гриффиндор", "Красный"));
        facultyRepository.save(new Faculty("Слизерин", "Зелёный"));
        facultyRepository.save(new Faculty("Когтевран", "Синий"));

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
    }

    // ---------- 4. PUT /faculties ----------
    @Test
    void shouldUpdateFaculty() {
        Faculty faculty = new Faculty("Пуффендуй", "Жёлтый");
        Faculty created = facultyRepository.save(faculty);
        created.setColor("Золотой");

        HttpEntity<Faculty> request = new HttpEntity<>(created);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Золотой");
    }

    // ---------- 5. DELETE /faculties/{id} ----------
    @Test
    void shouldDeleteFaculty() {
        Faculty faculty = new Faculty("Когтевран", "Синий");
        Faculty created = facultyRepository.save(faculty);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    // ---------- 6. GET /faculties/filter?nameOrColor= ----------
    @Test
    void shouldGetFacultiesByNameOrColor() {
        facultyRepository.save(new Faculty("Гриффиндор", "Красный"));
        facultyRepository.save(new Faculty("Слизерин", "Зелёный"));
        facultyRepository.save(new Faculty("Когтевран", "Синий"));
        facultyRepository.save(new Faculty("Пуффендуй", "Жёлтый"));

        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/filter?nameOrColor=Красный",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Гриффиндор");
    }

    // ---------- 7. Негативный кейс: 404 ----------
    @Test
    void shouldReturn404WhenFacultyNotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/999",
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
