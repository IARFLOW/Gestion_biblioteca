package com.example.tarea6.controllers;

import com.example.tarea6.models.Libro;
import com.example.tarea6.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroRepository libroRepository;
    
    // Constructor para inicializar datos de ejemplo
    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
        
        // Solo agregar datos iniciales si la tabla está vacía
        if (libroRepository.count() == 0) {
            libroRepository.save(new Libro(0, "Cien años de soledad", "Gabriel García Márquez", "9780307474728", 1967, "Realismo mágico", 5));
            libroRepository.save(new Libro(0, "Don Quijote de la Mancha", "Miguel de Cervantes", "9788420412146", 1605, "Novela", 3));
            libroRepository.save(new Libro(0, "El señor de los anillos", "J.R.R. Tolkien", "9788445073735", 1954, "Fantasía", 2));
            libroRepository.save(new Libro(0, "1984", "George Orwell", "9788499890944", 1949, "Ciencia ficción", 4));
            libroRepository.save(new Libro(0, "El principito", "Antoine de Saint-Exupéry", "9788478887194", 1943, "Literatura infantil", 6));
        }
    }
    
    // GET - Obtener todos los libros
    @GetMapping
    public List<Libro> getLibros() {
        return libroRepository.findAll();
    }
    
    // GET - Obtener un libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable int id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // GET - Buscar libros por título
    @GetMapping("/buscar/titulo/{titulo}")
    public List<Libro> buscarPorTitulo(@PathVariable String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    // GET - Buscar libros por autor
    @GetMapping("/buscar/autor/{autor}")
    public List<Libro> buscarPorAutor(@PathVariable String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }
    
    // GET - Buscar libros por género
    @GetMapping("/buscar/genero/{genero}")
    public List<Libro> buscarPorGenero(@PathVariable String genero) {
        return libroRepository.findByGeneroIgnoreCase(genero);
    }
    
    // GET - Buscar libros disponibles
    @GetMapping("/disponibles")
    public List<Libro> librosDisponibles() {
        return libroRepository.findByCopiasDisponiblesGreaterThan(0);
    }
    
    // POST - Crear un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> addLibro(@RequestBody Libro libro) {
        // Asegurar que estamos creando un nuevo libro
        libro.setId(0);
        Libro savedLibro = libroRepository.save(libro);
        return new ResponseEntity<>(savedLibro, HttpStatus.CREATED);
    }
    
    // PUT - Actualizar un libro existente
    @PutMapping("/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable int id, @RequestBody Libro libro) {
        if (!libroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        libro.setId(id);
        Libro updatedLibro = libroRepository.save(libro);
        return ResponseEntity.ok(updatedLibro);
    }
    
    // DELETE - Eliminar un libro
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLibro(@PathVariable int id) {
        if (!libroRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        libroRepository.deleteById(id);
        return ResponseEntity.ok("Libro eliminado con éxito");
    }
}