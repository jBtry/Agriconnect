package Gestionnaire;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Gestionnaire.ConnexionBD.connexion;
import static Gestionnaire.RequeteSQL.*;

/**
 * Gère une liste des capteurs associés et enregistre leurs relevés
 */
public class GestionnaireImpl extends UnicastRemoteObject implements Gestionnaire {

    /** Contient la connexion/session avec la base de données afin d'effectuer des opérations sur celle-ci */
    private Connection c;

    /** Un gestionnaire est caractérisé par un nom */
    private String nom;

    public GestionnaireImpl() throws RemoteException, SQLException, ClassNotFoundException {
        super();
        this.c = connexion();
        nom = "Gestionnaire A";
    }

    @Override
    public String toString() {
        return "GestionnaireImpl{" +
                "nom='" + nom + '\'' +
                '}';
    }

    /**
     * Permet d'enregistrer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param id  chaine de caractère identifiant le capteur ayant fait le relevé.
     * @param gps tableau contenant la position du capteur (latitude et longitude).
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    @Override
    public void enregistrerCapteur(String id, float[] gps) throws RemoteException, SQLException {
        PreparedStatement instructions = c.prepareStatement(INSERTION_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, id);
        instructions.setFloat(2, gps[0]);
        instructions.setFloat(3, gps[1]);
        instructions.executeUpdate();
    }

    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param id chaine de caractère identifiant le capteur ayant fait le relevé.
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    @Override
    public void retirerCapteur(String id) throws RemoteException, SQLException {
        PreparedStatement instructions = c.prepareStatement(SUPPRESSION_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, id);
        instructions.executeUpdate();
    }

    /**
     * Permet d'enregistrer les informations dans la Base de données contenant les valeurs relevées par les capteurs.
     * @param id  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp flottant représentant la température relevé par le capteur.
     * @param tauxH flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    @Override
    public void enregistrerValeur(String id, float temp, float tauxH, String horodatage) throws RemoteException, SQLException  {
        PreparedStatement instructions = c.prepareStatement(INSERTION_RELEVE); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, id);
        instructions.setFloat(2,temp);
        instructions.setFloat(3,tauxH);
        instructions.setString(4, horodatage);
        instructions.executeUpdate();
    }
}
