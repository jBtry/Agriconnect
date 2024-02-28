package Gestionnaire;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static Gestionnaire.RequeteSQL.CREATION_TABLE_CAPTEURS;
import static Gestionnaire.RequeteSQL.CREATION_TABLE_RELEVES;

/**
 * Permet de se connecter à la base de données
 * et aussi de créer les tables nécessaires si elles n'existent pas déja.
 * La base de données utilisée est SQLite
 */
public abstract class ConnexionBD {

    /** On obtient le répertoire de travail actuel de l'utilisateur */
    private static final String PWD = System.getProperty("user.dir");
    /** Nom de la base de données contenant les tables */
    private static final String NOM_BD = "Agriconnect.db";
    /** URL de la base de données */
    private static final String URL = "jdbc:sqlite:"+PWD+"/"+NOM_BD;

    /**
     * Crée une connexion à la base de données SQLite "Capteurs"
     * et crée la table contenant la liste des capteurs, si celle-ci n'existe pas deja
     * @return une connexion à la base de données SQLite "Capteurs"
     */
    public static Connection connexion() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC"); // Chargement du driver JDBC SQLite
        Connection c = DriverManager.getConnection(URL); // Établissement de la connexion sur la BD Agriconnect
        System.out.println("Connexion à la base de données SQLite établie.");
        Statement instructions = c.createStatement();
        instructions.execute(CREATION_TABLE_CAPTEURS); // Création de la table CAPTEURS si elle n'existe pas
        instructions.execute(CREATION_TABLE_RELEVES);  // Création de la table RELEVES si elle n'existe pas
        return c;
    }
}
