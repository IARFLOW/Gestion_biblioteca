package com.example.demo.controllers;

import com.example.demo.models.Alumno;
import com.example.demo.repositories.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    // Constructor para inicializar datos de ejemplo si no hay datos
    public AlumnoController(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
        
        // Solo agregar datos iniciales si la tabla está vacía
        if (alumnoRepository.count() == 0) {
            alumnoRepository.save(new Alumno(0, "Juan", 20));
            alumnoRepository.save(new Alumno(0, "María", 22));
            alumnoRepository.save(new Alumno(0, "Pedro", 21));
            alumnoRepository.save(new Alumno(0, "Ana", 19));
            alumnoRepository.save(new Alumno(0, "Carlos", 23));
        }
    }
    
    // GET - Obtener todos los alumnos
    @GetMapping
    public List<Alumno> getAlumnos() {
        return alumnoRepository.findAll();
    }
    
    // GET - Obtener un alumno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumno(@PathVariable int id) {
        Optional<Alumno> alumno = alumnoRepository.findById(id);
        return alumno.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // POST - Crear un nuevo alumno
    @PostMapping
    public ResponseEntity<Alumno> addAlumno(@RequestBody Alumno alumno) {
        // Asegurar que estamos creando un nuevo alumno
        alumno.setId(0);
        Alumno savedAlumno = alumnoRepository.save(alumno);
        return new ResponseEntity<>(savedAlumno, HttpStatus.CREATED);
    }
    
    // PUT - Actualizar un alumno existente
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        if (!alumnoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        alumno.setId(id);
        Alumno updatedAlumno = alumnoRepository.save(alumno);
        return ResponseEntity.ok(updatedAlumno);
    }
    
    // DELETE - Eliminar un alumno
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlumno(@PathVariable int id) {
        if (!alumnoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        alumnoRepository.deleteById(id);
        return ResponseEntity.ok("Alumno eliminado con éxito");
    }
}