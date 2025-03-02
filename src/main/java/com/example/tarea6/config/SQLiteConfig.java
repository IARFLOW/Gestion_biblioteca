package com.example.tarea6.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class SQLiteConfig {

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:biblioteca.db")
                .build();
                
        // Crear las tablas si no existen
        try (Connection conn = dataSource.getConnection(); 
             Statement stmt = conn.createStatement()) {
            
            // Tabla de libros
            stmt.execute("CREATE TABLE IF NOT EXISTS libros (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "titulo TEXT NOT NULL," +
                         "autor TEXT NOT NULL," +
                         "isbn TEXT UNIQUE," +
                         "anio_publicacion INTEGER," +
                         "genero TEXT," +
                         "copias_disponibles INTEGER DEFAULT 1" +
                         ")");
            
            // Tabla de préstamos
            stmt.execute("CREATE TABLE IF NOT EXISTS prestamos (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "libro_id INTEGER NOT NULL," +
                         "nombre_usuario TEXT NOT NULL," +
                         "fecha_prestamo TEXT NOT NULL," +
                         "fecha_devolucion_esperada TEXT NOT NULL," +
                         "fecha_devolucion_real TEXT," +
                         "devuelto BOOLEAN DEFAULT 0," +
                         "FOREIGN KEY (libro_id) REFERENCES libros(id)" +
                         ")");
                         
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
        
        return dataSource;
    }
}