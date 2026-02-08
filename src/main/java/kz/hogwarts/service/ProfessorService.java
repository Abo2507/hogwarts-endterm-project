package kz.hogwarts.service;

import kz.hogwarts.dto.ProfessorCreateDTO;
import kz.hogwarts.model.Professor;
import kz.hogwarts.patterns.factory.PersonFactory;
import kz.hogwarts.repository.ProfessorRepository;
import kz.hogwarts.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor getProfessorById(Integer id) {
        ValidationUtils.validatePositive(id, "Professor ID");
        return professorRepository.findById(id);
    }

    public Professor createProfessor(ProfessorCreateDTO dto) {
        ValidationUtils.validateNotEmpty(dto.getName(), "Professor name");
        ValidationUtils.validatePositive(dto.getAge(), "Age");
        ValidationUtils.validateNotEmpty(dto.getSubject(), "Subject");
        ValidationUtils.validateNotNull(dto.getSalary(), "Salary");

        // Using Factory Pattern to create Professor
        Professor professor = (Professor) PersonFactory.createProfessor(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getSubject(),
                dto.getSalary()
        );

        return professorRepository.save(professor);
    }

    public Professor updateProfessor(Integer id, ProfessorCreateDTO dto) {
        ValidationUtils.validatePositive(id, "Professor ID");
        ValidationUtils.validateNotEmpty(dto.getName(), "Professor name");
        ValidationUtils.validatePositive(dto.getAge(), "Age");
        ValidationUtils.validateNotEmpty(dto.getSubject(), "Subject");
        ValidationUtils.validateNotNull(dto.getSalary(), "Salary");

        Professor professor = (Professor) PersonFactory.createProfessor(
                dto.getName(),
                dto.getAge(),
                dto.getHouseId(),
                dto.getSubject(),
                dto.getSalary()
        );

        return professorRepository.update(id, professor);


    }

    public void deleteProfessor(Integer id) {
        ValidationUtils.validatePositive(id, "Professor ID");
        professorRepository.delete(id);
    }
}