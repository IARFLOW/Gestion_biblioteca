package client.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import org.json.*;

public class AlumnoGUI extends javax.swing.JFrame {

    // URL del servicio REST
    private static final String BASE_URL = "http://localhost:8080/alumnos";

    // Componentes de la interfaz que vamos a agregar
    private JTable alumnosTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nombreField, edadField;
    private JButton refreshButton, getByIdButton, addButton, updateButton, deleteButton, clearButton;
    private JPanel formPanel, buttonPanel, tablePanel;

    public AlumnoGUI() {
        // No modificar esto - es generado por NetBeans
        initComponents();

        // Agregar nuestras personalizaciones DESPUÉS
        setupAdditionalComponents();
        setupLayout();
        setupListeners();

        // Configuración adicional de la ventana
        setTitle("Gestión de Alumnos");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Cargar datos iniciales
        try {
            refreshAlumnos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar al servidor. Verifique que esté en ejecución.",
                    "Error de conexión",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // Este método configura nuestros componentes adicionales
    private void setupAdditionalComponents() {
        // Tabla de alumnos
        String[] columns = {"ID", "Nombre", "Edad"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable directamente
            }
        };
        alumnosTable = new JTable(tableModel);
        alumnosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alumnosTable.getTableHeader().setReorderingAllowed(false);

        // Campos de texto
        idField = new JTextField(10);
        idField.setEditable(false); // El ID no se edita manualmente
        nombreField = new JTextField(20);
        edadField = new JTextField(5);

        // Botones
        refreshButton = new JButton("Refrescar Lista");
        getByIdButton = new JButton("Buscar por ID");
        addButton = new JButton("Añadir");
        updateButton = new JButton("Actualizar");
        deleteButton = new JButton("Eliminar");
        clearButton = new JButton("Limpiar");
    }

    // Este método configura el layout
    private void setupLayout() {
        // Limpiamos el layout autogenerado por NetBeans
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(10, 10));

        // Panel del formulario
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Alumno"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(idField, gbc);

        // Fila 2: Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nombreField, gbc);

        // Fila 3: Edad
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Edad:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(edadField, gbc);

        // Panel de botones
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(refreshButton);
        buttonPanel.add(getByIdButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Panel de la tabla
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Lista de Alumnos"));
        tablePanel.add(new JScrollPane(alumnosTable), BorderLayout.CENTER);

        // Añadir todo al frame principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(tablePanel, BorderLayout.CENTER);

        pack();
    }

    // Este método configura los listeners de eventos
    private void setupListeners() {
        // Listener para selección en la tabla
        alumnosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && alumnosTable.getSelectedRow() != -1) {
                int row = alumnosTable.getSelectedRow();
                idField.setText(alumnosTable.getValueAt(row, 0).toString());
                nombreField.setText(alumnosTable.getValueAt(row, 1).toString());
                edadField.setText(alumnosTable.getValueAt(row, 2).toString());
            }
        });

        // Listeners para los botones
        refreshButton.addActionListener(e -> {
            try {
                refreshAlumnos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al obtener la lista de alumnos: " + ex.getMessage(),
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        getByIdButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Introduce el ID del alumno:");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    getAlumnoById(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addButton.addActionListener(e -> {
            if (validateForm(false)) {
                addAlumno();
            }
        });

        updateButton.addActionListener(e -> {
            if (validateForm(true)) {
                updateAlumno();
            }
        });

        deleteButton.addActionListener(e -> {
            if (!idField.getText().isEmpty()) {
                int option = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que quieres eliminar este alumno?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    deleteAlumno();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un alumno para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> clearForm());
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private boolean validateForm(boolean isUpdate) {
        // Para actualizar necesitamos un ID
        if (isUpdate && idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un alumno para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar nombre
        if (nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar edad
        try {
            int edad = Integer.parseInt(edadField.getText().trim());
            if (edad < 0 || edad > 120) {
                JOptionPane.showMessageDialog(this, "La edad debe estar entre 0 y 120", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void refreshAlumnos() throws Exception {
        // Todo el código, sin el try-catch
        String response = sendRequest(BASE_URL, "GET", null);
        JSONArray jsonArray = new JSONArray(response);

        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Llenar con los nuevos datos
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Object[] row = {
                jsonObject.getInt("id"),
                jsonObject.getString("nombre"),
                jsonObject.getInt("edad")
            };
            tableModel.addRow(row);
        }

        // Limpiar el formulario
        clearForm();
    }

    private void getAlumnoById(int id) {
        try {
            String response = sendRequest(BASE_URL + "/" + id, "GET", null);
            JSONObject jsonObject = new JSONObject(response);

            // Actualizar el formulario
            idField.setText(String.valueOf(jsonObject.getInt("id")));
            nombreField.setText(jsonObject.getString("nombre"));
            edadField.setText(String.valueOf(jsonObject.getInt("edad")));

            // Seleccionar la fila correspondiente en la tabla
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (Integer.parseInt(tableModel.getValueAt(i, 0).toString()) == id) {
                    alumnosTable.setRowSelectionInterval(i, i);
                    break;
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener el alumno: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAlumno() {
        try {
            // Crear el JSON con los datos del alumno
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nombre", nombreField.getText().trim());
            jsonObject.put("edad", Integer.parseInt(edadField.getText().trim()));

            // Enviar la solicitud POST
            String response = sendRequest(BASE_URL, "POST", jsonObject.toString());

            // Refrescar la tabla
            refreshAlumnos();

            JOptionPane.showMessageDialog(this, "Alumno añadido correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al añadir el alumno: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAlumno() {
        try {
            int id = Integer.parseInt(idField.getText());

            // Crear el JSON con los datos del alumno
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nombre", nombreField.getText().trim());
            jsonObject.put("edad", Integer.parseInt(edadField.getText().trim()));

            // Enviar la solicitud PUT
            String response = sendRequest(BASE_URL + "/" + id, "PUT", jsonObject.toString());

            // Refrescar la tabla
            refreshAlumnos();

            JOptionPane.showMessageDialog(this, "Alumno actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar el alumno: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAlumno() {
        try {
            int id = Integer.parseInt(idField.getText());

            // Enviar la solicitud DELETE
            String response = sendRequest(BASE_URL + "/" + id, "DELETE", null);

            // Refrescar la tabla
            refreshAlumnos();

            JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar el alumno: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        idField.setText("");
        nombreField.setText("");
        edadField.setText("");
        alumnosTable.clearSelection();
    }

    private String sendRequest(String urlStr, String method, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        if (jsonBody != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        int responseCode = conn.getResponseCode();

        if (responseCode >= 200 && responseCode < 300) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder error = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    error.append(errorLine.trim());
                }
                throw new Exception("HTTP error code: " + responseCode + ", message: " + error.toString());
            }
        }
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
