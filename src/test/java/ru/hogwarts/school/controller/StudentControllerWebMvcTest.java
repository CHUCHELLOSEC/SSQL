package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- 1. POST /students ----------
    @Test
    void shouldCreateStudent() throws Exception {
        Student student = new Student("Гарри Поттер", 11);
        Student saved = new Student(1L, "Гарри Поттер", 11);
        when(studentService.addStudent(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(11));
    }

    // ---------- 2. GET /students/{id} ----------
    @Test
    void shouldGetStudentById() throws Exception {
        Student student = new Student(1L, "Гермиона Грейнджер", 12);
        when(studentService.getStudent(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гермиона Грейнджер"))
                .andExpect(jsonPath("$.age").value(12));
    }

    // ---------- 3. GET /students (все) ----------
    @Test
    void shouldGetAllStudents() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Гарри", 11),
                new Student(2L, "Гермиона", 12),
                new Student(3L, "Рон", 11)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Гарри"))
                .andExpect(jsonPath("$[1].name").value("Гермиона"))
                .andExpect(jsonPath("$[2].name").value("Рон"));
    }

    // ---------- 4. PUT /students ----------
    @Test
    void shouldUpdateStudent() throws Exception {
        Student student = new Student(1L, "Невилл Долгопупс", 10);
        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Невилл Долгопупс"))
                .andExpect(jsonPath("$.age").value(10));
    }

    // ---------- 5. DELETE /students/{id} ----------
    @Test
    void shouldDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/{id}", 1))
                .andExpect(status().isNoContent());
    }

    // ---------- 6. GET /students/filter?min=&max= ----------
    @Test
    void shouldGetStudentsByAgeBetween() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Гарри", 11),
                new Student(2L, "Рон", 11)
        );
        when(studentService.getStudentsByAgeBetween(11, 12)).thenReturn(students);

        mockMvc.perform(get("/students/filter")
                        .param("min", "11")
                        .param("max", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри"))
                .andExpect(jsonPath("$[1].name").value("Рон"));
    }

    // ---------- 7. Негативный кейс: 404 ----------
    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {
        when(studentService.getStudent(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
