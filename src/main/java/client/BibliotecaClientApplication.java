package client;


import com.example.JFrame.BibliotecaJFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BibliotecaClientApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentar usar el Look and Feel del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Crear y mostrar la ventana de la aplicación
            BibliotecaJFrame gui = new BibliotecaJFrame();
            gui.setVisible(true);
            
            System.out.println("Sistema de Gestión de Biblioteca iniciado");
        });
    }
}