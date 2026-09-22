package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.debug("Was invoked method for create faculty: name={}, color={}",
                faculty.getName(), faculty.getColor());
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFaculty(Long id) {
        logger.debug("Was invoked method for get faculty by id={}", id);
        return facultyRepository.findById(id);
    }

    public List<Faculty> getAllFaculties() {
        logger.debug("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.debug("Was invoked method for update faculty with id={}", faculty.getId());

        if (facultyRepository.existsById(faculty.getId())) {
            return facultyRepository.save(faculty);
        }

        // Ошибка: факультета для обновления не существует
        logger.error("No faculty with id={} to update", faculty.getId());
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.debug("Was invoked method for delete faculty with id={}", id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> getFacultiesByNameOrColor(String nameOrColor) {
        logger.debug("Was invoked method for find faculties by name or color={}", nameOrColor);
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(nameOrColor, nameOrColor);
    }

    public List<Student> getStudentsByFacultyId(Long facultyId) {
        logger.debug("Was invoked method for get students of faculty with id={}", facultyId);
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElse(new ArrayList<>());
    }
}