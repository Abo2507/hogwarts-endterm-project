package kz.hogwarts.controller;

import jakarta.validation.Valid;
import kz.hogwarts.dto.ProfessorCreateDTO;
import kz.hogwarts.model.Professor;
import kz.hogwarts.service.ProfessorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessors() {
        List<Professor> professors = professorService.getAllProfessors();
        return ResponseEntity.ok(professors);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Integer id) {
        Professor professor = professorService.getProfessorById(id);
        return ResponseEntity.ok(professor);
    }

    @PostMapping
    public ResponseEntity<Professor> createProfessor(@Valid @RequestBody ProfessorCreateDTO dto) {
        Professor createdProfessor = professorService.createProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(
            @PathVariable Integer id,
            @Valid @RequestBody ProfessorCreateDTO dto) {
        Professor updatedProfessor = professorService.updateProfessor(id, dto);
        return ResponseEntity.ok(updatedProfessor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Integer id) {
        professorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }
}