package com.example.demo.config;

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
                .url("jdbc:sqlite:alumnos.db")
                .build();
                
        // Crear la tabla si no existe
        try (Connection conn = dataSource.getConnection(); 
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS alumnos (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nombre TEXT NOT NULL," +
                         "edad INTEGER NOT NULL" +
                         ")");
                         
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
        
        return dataSource;
    }
}