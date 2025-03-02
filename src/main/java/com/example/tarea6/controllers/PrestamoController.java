package com.example.tarea6.controllers;

import com.example.tarea6.models.Libro;
import com.example.tarea6.models.Prestamo;
import com.example.tarea6.repositories.LibroRepository;
import com.example.tarea6.repositories.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {
    
    @Autowired
    private PrestamoRepository prestamoRepository;
    
    @Autowired
    private LibroRepository libroRepository;
    
    // GET - Obtener todos los préstamos
    @GetMapping
    public List<Prestamo> getPrestamos() {
        return prestamoRepository.findAll();
    }
    
    // GET - Obtener un préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> getPrestamo(@PathVariable int id) {
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        return prestamo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // GET - Obtener préstamos activos (no devueltos)
    @GetMapping("/activos")
    public List<Prestamo> getPrestamosActivos() {
        return prestamoRepository.findByDevueltoFalse();
    }
    
    // GET - Obtener préstamos atrasados
    @GetMapping("/atrasados")
    public List<Prestamo> getPrestamosAtrasados() {
        return prestamoRepository.findByFechaDevolucionEsperadaBeforeAndDevueltoFalse(LocalDate.now());
    }
    
    // GET - Obtener préstamos por usuario
    @GetMapping("/usuario/{nombreUsuario}")
    public List<Prestamo> getPrestamosPorUsuario(@PathVariable String nombreUsuario) {
        return prestamoRepository.findByNombreUsuarioIgnoreCase(nombreUsuario);
    }
    
    // POST - Realizar un préstamo
    @PostMapping
    public ResponseEntity<?> realizarPrestamo(@RequestBody PrestamoRequest prestamoRequest) {
        // Buscar el libro
        Optional<Libro> libroOpt = libroRepository.findById(prestamoRequest.getLibroId());
        
        if (!libroOpt.isPresent()) {
            return ResponseEntity.badRequest().body("El libro no existe");
        }
        
        Libro libro = libroOpt.get();
        
        // Verificar si hay copias disponibles
        if (libro.getCopiasDisponibles() <= 0) {
            return ResponseEntity.badRequest().body("No hay copias disponibles de este libro");
        }
        
        // Crear el nuevo préstamo
        LocalDate fechaPrestamo = LocalDate.now();
        // Por defecto, el préstamo es por 14 días
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(14);
        
        Prestamo prestamo = new Prestamo(libro, prestamoRequest.getNombreUsuario(), fechaPrestamo, fechaDevolucion);
        
        // Actualizar las copias disponibles
        libro.setCopiasDisponibles(libro.getCopiasDisponibles() - 1);
        libroRepository.save(libro);
        
        // Guardar el préstamo
        Prestamo nuevoPrestamo = prestamoRepository.save(prestamo);
        
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }
    
    // PUT - Devolver un libro
    @PutMapping("/devolver/{id}")
    public ResponseEntity<?> devolverLibro(@PathVariable int id) {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(id);
        
        if (!prestamoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Prestamo prestamo = prestamoOpt.get();
        
        // Si el libro ya fue devuelto
        if (prestamo.isDevuelto()) {
            return ResponseEntity.badRequest().body("Este libro ya fue devuelto");
        }
        
        // Actualizar el estado del préstamo
        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionReal(LocalDate.now());
        
        // Actualizar el número de copias disponibles del libro
        Libro libro = prestamo.getLibro();
        libro.setCopiasDisponibles(libro.getCopiasDisponibles() + 1);
        libroRepository.save(libro);
        
        // Guardar el préstamo actualizado
        prestamoRepository.save(prestamo);
        
        return ResponseEntity.ok(prestamo);
    }
    
    // Clase auxiliar para la solicitud de préstamo
    public static class PrestamoRequest {
        private int libroId;
        private String nombreUsuario;
        
        public int getLibroId() {
            return libroId;
        }
        
        public void setLibroId(int libroId) {
            this.libroId = libroId;
        }
        
        public String getNombreUsuario() {
            return nombreUsuario;
        }
        
        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }
    }
}