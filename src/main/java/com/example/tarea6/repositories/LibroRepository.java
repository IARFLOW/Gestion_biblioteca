package com.example.tarea6.repositories;

import com.example.tarea6.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {
    // Buscar libros por título (búsqueda parcial)
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    
    // Buscar libros por autor (búsqueda parcial)
    List<Libro> findByAutorContainingIgnoreCase(String autor);
    
    // Buscar libros por género
    List<Libro> findByGeneroIgnoreCase(String genero);
    
    // Buscar libros por ISBN
    Libro findByIsbn(String isbn);
    
    // Buscar libros con copias disponibles
    List<Libro> findByCopiasDisponiblesGreaterThan(int copias);
}
