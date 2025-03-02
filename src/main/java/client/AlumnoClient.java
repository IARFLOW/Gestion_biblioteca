package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AlumnoClient {
    private static final String BASE_URL = "http://localhost:8080/alumnos";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Obtener todos los alumnos");
            System.out.println("2. Obtener alumno por ID");
            System.out.println("3. Agregar alumno");
            System.out.println("4. Actualizar alumno");
            System.out.println("5. Eliminar alumno");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    getAlumnos();
                    break;
                case 2:
                    System.out.print("Ingrese el ID del alumno: ");
                    int id = scanner.nextInt();
                    getAlumno(id);
                    break;
                case 3:
                    System.out.print("Ingrese el nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese la edad: ");
                    int edad = scanner.nextInt();
                    addAlumno(new Alumno2(nombre, edad));
                    break;
                case 4:
                    System.out.print("Ingrese el ID del alumno a actualizar: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Ingrese el nuevo nombre: ");
                    nombre = scanner.nextLine();
                    System.out.print("Ingrese la nueva edad: ");
                    edad = scanner.nextInt();
                    updateAlumno(id, new Alumno2(nombre, edad));
                    break;
                case 5:
                    System.out.print("Ingrese el ID del alumno a eliminar: ");
                    id = scanner.nextInt();
                    deleteAlumno(id);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void getAlumnos() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void getAlumno(int id) throws IOException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        printResponse(conn);
    }

    private static void addAlumno(Alumno2 alumno) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        sendRequest(conn, alumno.toJson());
        printResponse(conn);
    }

    private static void updateAlumno(int id, Alumno2 alumno) throws IOException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        sendRequest(conn, alumno.toJson());
        printResponse(conn);
    }

    private static void deleteAlumno(int id) throws IOException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
        }
    }
}

class Alumno2 {
    private int id;
    private String nombre;
    private int edad;

    public Alumno2(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String toJson() {
        return "{\"nombre\":\"" + nombre + "\",\"edad\":" + edad + "}";
    }
}
