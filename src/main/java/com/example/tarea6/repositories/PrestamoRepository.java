package com.example.tarea6.repositories;

import com.example.tarea6.models.Libro;
import com.example.tarea6.models.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    // Buscar préstamos por nombre de usuario
    List<Prestamo> findByNombreUsuarioIgnoreCase(String nombreUsuario);
    
    // Buscar préstamos activos (no devueltos)
    List<Prestamo> findByDevueltoFalse();
    
    // Buscar préstamos atrasados (fecha de devolución esperada menor a hoy y no devuelto)
    List<Prestamo> findByFechaDevolucionEsperadaBeforeAndDevueltoFalse(LocalDate fecha);
    
    // Buscar préstamos por libro
    List<Prestamo> findByLibro(Libro libro);
    
    // Buscar préstamos por libro y no devueltos
    List<Prestamo> findByLibroAndDevueltoFalse(Libro libro);
}