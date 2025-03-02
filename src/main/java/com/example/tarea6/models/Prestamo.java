package com.example.tarea6.models;

import com.example.tarea6.config.SQLiteDateConverter;
import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;
    private String nombreUsuario;
    @Column(name = "fecha_prestamo")
    @Convert(converter = SQLiteDateConverter.class)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion_esperada")
    @Convert(converter = SQLiteDateConverter.class)
    private LocalDate fechaDevolucionEsperada;

    @Column(name = "fecha_devolucion_real")
    @Convert(converter = SQLiteDateConverter.class)
    private LocalDate fechaDevolucionReal;
    private boolean devuelto;

    // Constructor por defecto requerido por JPA
    public Prestamo() {
    }

    public Prestamo(Libro libro, String nombreUsuario, LocalDate fechaPrestamo, LocalDate fechaDevolucionEsperada) {
        this.libro = libro;
        this.nombreUsuario = nombreUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.devuelto = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}
