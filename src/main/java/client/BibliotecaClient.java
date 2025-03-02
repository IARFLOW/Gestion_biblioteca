package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BibliotecaClient {
    private static final String BASE_URL_LIBROS = "http://localhost:8080/libros";
    private static final String BASE_URL_PRESTAMOS = "http://localhost:8080/prestamos";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===");
            System.out.println("1. Gestión de Libros");
            System.out.println("2. Gestión de Préstamos");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    gestionLibros(scanner);
                    break;
                case 2:
                    gestionPrestamos(scanner);
                    break;
                case 3:
                    System.out.println("¡Gracias por usar el sistema de biblioteca!");
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }
    
    private static void gestionLibros(Scanner scanner) throws IOException {
        while (true) {
            System.out.println("\n--- GESTIÓN DE LIBROS ---");
            System.out.println("1. Ver todos los libros");
            System.out.println("2. Buscar libro por ID");
            System.out.println("3. Buscar libros por título");
            System.out.println("4. Buscar libros por autor");
            System.out.println("5. Buscar libros por género");
            System.out.println("6. Ver libros disponibles");
            System.out.println("7. Agregar libro");
            System.out.println("8. Actualizar libro");
            System.out.println("9. Eliminar libro");
            System.out.println("10. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    getLibros();
                    break;
                case 2:
                    System.out.print("Ingrese el ID del libro: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    getLibro(id);
                    break;
                case 3:
                    System.out.print("Ingrese el título a buscar: ");
                    String titulo = scanner.nextLine();
                    buscarLibrosPorTitulo(titulo);
                    break;
                case 4:
                    System.out.print("Ingrese el autor a buscar: ");
                    String autor = scanner.nextLine();
                    buscarLibrosPorAutor(autor);
                    break;
                case 5:
                    System.out.print("Ingrese el género a buscar: ");
                    String genero = scanner.nextLine();
                    buscarLibrosPorGenero(genero);
                    break;
                case 6:
                    verLibrosDisponibles();
                    break;
                case 7:
                    System.out.print("Ingrese el título: ");
                    String nuevoTitulo = scanner.nextLine();
                    System.out.print("Ingrese el autor: ");
                    String nuevoAutor = scanner.nextLine();
                    System.out.print("Ingrese el ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Ingrese el año de publicación: ");
                    int anio = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el género: ");
                    String nuevoGenero = scanner.nextLine();
                    System.out.print("Ingrese la cantidad de copias disponibles: ");
                    int copias = scanner.nextInt();
                    scanner.nextLine();
                    
                    agregarLibro(nuevoTitulo, nuevoAutor, isbn, anio, nuevoGenero, copias);
                    break;
                case 8:
                    System.out.print("Ingrese el ID del libro a actualizar: ");
                    int libroId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nuevo título: ");
                    String tituloActualizado = scanner.nextLine();
                    System.out.print("Ingrese el nuevo autor: ");
                    String autorActualizado = scanner.nextLine();
                    System.out.print("Ingrese el nuevo ISBN: ");
                    String isbnActualizado = scanner.nextLine();
                    System.out.print("Ingrese el nuevo año de publicación: ");
                    int anioActualizado = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nuevo género: ");
                    String generoActualizado = scanner.nextLine();
                    System.out.print("Ingrese la nueva cantidad de copias: ");
                    int copiasActualizado = scanner.nextInt();
                    scanner.nextLine();
                    
                    actualizarLibro(libroId, tituloActualizado, autorActualizado, isbnActualizado, anioActualizado, generoActualizado, copiasActualizado);
                    break;
                case 9:
                    System.out.print("Ingrese el ID del libro a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    scanner.nextLine();
                    eliminarLibro(idEliminar);
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }
    
    private static void gestionPrestamos(Scanner scanner) throws IOException {
        while (true) {
            System.out.println("\n--- GESTIÓN DE PRÉSTAMOS ---");
            System.out.println("1. Ver todos los préstamos");
            System.out.println("2. Ver préstamo por ID");
            System.out.println("3. Ver préstamos activos");
            System.out.println("4. Ver préstamos atrasados");
            System.out.println("5. Ver préstamos por usuario");
            System.out.println("6. Realizar préstamo");
            System.out.println("7. Devolver libro");
            System.out.println("8. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    getPrestamos();
                    break;
                case 2:
                    System.out.print("Ingrese el ID del préstamo: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    getPrestamo(id);
                    break;
                case 3:
                    getPrestamosActivos();
                    break;
                case 4:
                    getPrestamosAtrasados();
                    break;
                case 5:
                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombreUsuario = scanner.nextLine();
                    getPrestamosPorUsuario(nombreUsuario);
                    break;
                case 6:
                    System.out.print("Ingrese el ID del libro: ");
                    int libroId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nombre del usuario: ");
                    String usuario = scanner.nextLine();
                    realizarPrestamo(libroId, usuario);
                    break;
                case 7:
                    System.out.print("Ingrese el ID del préstamo: ");
                    int prestamoId = scanner.nextInt();
                    scanner.nextLine();
                    devolverLibro(prestamoId);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    // Métodos para gestión de libros
    private static void getLibros() throws IOException {
        URL url = new URL(BASE_URL_LIBROS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void getLibro(int id) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void buscarLibrosPorTitulo(String titulo) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/buscar/titulo/" + URLEncoder.encode(titulo, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void buscarLibrosPorAutor(String autor) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/buscar/autor/" + URLEncoder.encode(autor, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void buscarLibrosPorGenero(String genero) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/buscar/genero/" + URLEncoder.encode(genero, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void verLibrosDisponibles() throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/disponibles");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void agregarLibro(String titulo, String autor, String isbn, int anioPublicacion, String genero, int copiasDisponibles) throws IOException {
        URL url = new URL(BASE_URL_LIBROS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        
        String jsonLibro = String.format(
            "{\"titulo\":\"%s\",\"autor\":\"%s\",\"isbn\":\"%s\",\"anioPublicacion\":%d,\"genero\":\"%s\",\"copiasDisponibles\":%d}",
            titulo, autor, isbn, anioPublicacion, genero, copiasDisponibles
        );
        
        sendRequest(conn, jsonLibro);
        printResponse(conn);
    }

    private static void actualizarLibro(int id, String titulo, String autor, String isbn, int anioPublicacion, String genero, int copiasDisponibles) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        
        String jsonLibro = String.format(
            "{\"titulo\":\"%s\",\"autor\":\"%s\",\"isbn\":\"%s\",\"anioPublicacion\":%d,\"genero\":\"%s\",\"copiasDisponibles\":%d}",
            titulo, autor, isbn, anioPublicacion, genero, copiasDisponibles
        );
        
        sendRequest(conn, jsonLibro);
        printResponse(conn);
    }

    private static void eliminarLibro(int id) throws IOException {
        URL url = new URL(BASE_URL_LIBROS + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        printResponse(conn);
    }
    
    // Métodos para gestión de préstamos
    private static void getPrestamos() throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void getPrestamo(int id) throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void getPrestamosActivos() throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS + "/activos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void getPrestamosAtrasados() throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS + "/atrasados");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void getPrestamosPorUsuario(String nombreUsuario) throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS + "/usuario/" + URLEncoder.encode(nombreUsuario, "UTF-8"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }
    
    private static void realizarPrestamo(int libroId, String nombreUsuario) throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        
        String jsonPrestamo = String.format(
            "{\"libroId\":%d,\"nombreUsuario\":\"%s\"}",
            libroId, nombreUsuario
        );
        
        sendRequest(conn, jsonPrestamo);
        printResponse(conn);
    }
    
    private static void devolverLibro(int prestamoId) throws IOException {
        URL url = new URL(BASE_URL_PRESTAMOS + "/devolver/" + prestamoId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        printResponse(conn);
    }

    private static void sendRequest(HttpURLConnection conn, String jsonInput) throws IOException {
        conn.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private static void printResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream(), 
                "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println("Respuesta: " + response);
        }
    }
}