package com.example.demo.repositories;

import com.example.demo.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    // Las operaciones básicas CRUD se heredan de JpaRepository
    // Podemos añadir consultas personalizadas si fueran necesarias
}