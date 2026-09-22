package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.addFaculty(faculty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty updated = facultyService.updateFaculty(faculty);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Faculty>> getFacultiesByNameOrColor(
            @RequestParam String nameOrColor) {
        return ResponseEntity.ok(facultyService.getFacultiesByNameOrColor(nameOrColor));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudentsByFacultyId(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsByFacultyId(id);
        return students != null ? ResponseEntity.ok(students) : ResponseEntity.notFound().build();
    }
}