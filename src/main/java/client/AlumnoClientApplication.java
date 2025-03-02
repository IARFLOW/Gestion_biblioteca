package client;

import client.gui.AlumnoGUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AlumnoClientApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentar usar el Look and Feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Crear y mostrar la ventana de la aplicación
            AlumnoGUI gui = new AlumnoGUI();
            gui.setVisible(true);
            
            System.out.println("Aplicación de gestión de alumnos iniciada");
        });
    }
}