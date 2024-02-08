package Gestionnaire;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class GestionBD {

    public static void connect() {
        Connection conn = null;
        try {
            // Chemin de la base de données SQLite
            String url = "jdbc:sqlite:/home/linus/Documents/M1/JavaProjet/Agriconnect/info.db";

            // Créer une connexion à la base de données
            conn = DriverManager.getConnection(url);
            System.out.println("Connexion à SQLite établie.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        connect();
    }

}
