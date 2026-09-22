package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- 1. POST /faculties ----------
    @Test
    void shouldCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Красный");
        Faculty saved = new Faculty(1L, "Гриффиндор", "Красный");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    // ---------- 2. GET /faculties/{id} ----------
    @Test
    void shouldGetFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "Слизерин", "Зелёный");
        when(facultyService.getFaculty(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculties/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("Зелёный"));
    }

    // ---------- 3. GET /faculties (все) ----------
    @Test
    void shouldGetAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Гриффиндор", "Красный"),
                new Faculty(2L, "Слизерин", "Зелёный"),
                new Faculty(3L, "Когтевран", "Синий")
        );
        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[1].name").value("Слизерин"))
                .andExpect(jsonPath("$[2].name").value("Когтевран"));
    }

    // ---------- 4. PUT /faculties ----------
    @Test
    void shouldUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Пуффендуй", "Жёлтый");
        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Пуффендуй"))
                .andExpect(jsonPath("$.color").value("Жёлтый"));
    }

    // ---------- 5. DELETE /faculties/{id} ----------
    @Test
    void shouldDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/faculties/{id}", 1))
                .andExpect(status().isNoContent());
    }

    // ---------- 6. GET /faculties/filter?nameOrColor= ----------
    @Test
    void shouldGetFacultiesByNameOrColor() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "Гриффиндор", "Красный")
        );
        when(facultyService.getFacultiesByNameOrColor("Красный")).thenReturn(faculties);

        mockMvc.perform(get("/faculties/filter")
                        .param("nameOrColor", "Красный"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[0].color").value("Красный"));
    }

    // ---------- 7. Негативный кейс: 404 ----------
    @Test
    void shouldReturn404WhenFacultyNotFound() throws Exception {
        when(facultyService.getFaculty(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/faculties/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
