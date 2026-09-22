package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFaculty(Long id) {
        return facultyRepository.findById(id);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultiesByNameOrColor(String nameOrColor) {
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(nameOrColor, nameOrColor);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(new ArrayList<>());
    }
}