package Gestionnaire;

import java.rmi.RemoteException;
import java.sql.SQLException;

public interface Gestionnaire extends java.rmi.Remote {

    /**
     * Permet d'enregistrer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param id  chaine de caractère identifiant le capteur ayant fait le relevé.
     * @param gps tableau contenant la position du capteur (latitude et longitude).
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void enregistrerCapteur(String id, float[] gps) throws RemoteException, SQLException;

    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param id chaine de caractère identifiant le capteur ayant fait le relevé.
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void retirerCapteur(String id) throws RemoteException, SQLException;


    /**
     * Permet d'enregistrer les informations dans la Base de données contenant les valeurs relevées par les capteurs.
     * @param id  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp flottant représentant la température relevé par le capteur.
     * @param tauxH flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void enregistrerValeur(String id, float temp, float tauxH, String horodatage) throws RemoteException, SQLException;

}
