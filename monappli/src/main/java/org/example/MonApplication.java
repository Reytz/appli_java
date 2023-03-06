package org.example;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class MonApplication extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    private Timer timer;

    public MonApplication() {
        super();

        // Définit la taille et la position de la fenêtre
        setSize(300, 200);
        setLocationRelativeTo(null);

        // Définit le titre de la fenêtre
        setTitle("Mon application");

        // Crée un JPanel pour contenir le JLabel
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Color.WHITE);

        // Crée le JLabel
        label = new JLabel("Information de la base de données");

        // Ajoute le JLabel au JPanel
        panel.add(label);

        // Ajoute le JPanel à la fenêtre
        add(panel);

        // Définit le comportement de la fenêtre lorsqu'elle est fermée
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Empêche la fenêtre d'être redimensionnée
        setResizable(false);

        // Verrouille la fenêtre en premier plan
        setAlwaysOnTop(true);

        // Rend la fenêtre visible
        setVisible(true);

        // Définit un timer pour mettre à jour l'information de la base de données toutes les 2 secondes
        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Connexion à la base de données
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }catch (Exception exception){
                    exception.printStackTrace();
                }

                try {
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/clients", "alex", "alex");
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT COUNT(*) FROM contrats");
                    if (rs.next()) {
                        label.setText("Nombre de lignes dans la table : " + rs.getInt(1));
                    }
                } catch (SQLException ex) {
                    label.setText("Erreur de connexion à la base de données");
                }
                finally {
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                        if (stmt != null) {
                            stmt.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        // Ignorer les erreurs
                    }
                }
            }
        });
        timer.start();

        // Définit le comportement de la fenêtre lorsqu'elle est fermée
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                timer.stop();
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        MonApplication app = new MonApplication();
    }
}
