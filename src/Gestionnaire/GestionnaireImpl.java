package Gestionnaire;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Gestionnaire.ConnexionBD.connexion;

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
        nom = "Gestionnaire Agriconnect";
    }

    @Override
    public String toString() {
        return "GestionnaireImpl{" +
                "nom='" + nom + '\'' +
                '}';
    }

    /**
     * Permet d'ajouter un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException    si erreur lors de l'insertion dans la base de données.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String ajouterCapteur(String idCapteur) throws RemoteException, SQLException {
        return null;
    }

    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String retirerCapteur(String idCapteur) throws RemoteException {
        return null;
    }

    /**
     * Permet de démarrer l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String demarrerCapteur(String idCapteur) throws RemoteException {
        return null;
    }

    /**
     * Permet de stopper l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String stopperCapteur(String idCapteur) throws RemoteException {
        return null;
    }

    /**
     * Permet de lister les capteurs enregistrés par l'utilisateur
     * @return une chaine de caractère représentant la liste des capteurs
     * @throws SQLException    si erreur lors de l'insertion dans la base de données.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String listeCapteurs() throws RemoteException, SQLException {
        Statement instructions = c.createStatement();
        ResultSet retourSQL = instructions.executeQuery(RequeteSQL.SELECT_CAPTEUR);
        StringBuilder resultat = new StringBuilder();
        while(retourSQL.next()){
            resultat.append(retourSQL.getString("Id")).append(" ");
            resultat.append(retourSQL.getFloat("Latitude")).append(" ");
            resultat.append(retourSQL.getFloat("Longitude")).append("\n");
        }
        return resultat.toString();
    }

    /**
     * Permet de visualiser le dernier relevé d'un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère représentant le dernier relevé du capteur
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String dernierReleve(String idCapteur) throws RemoteException {
        return null;
    }

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée (1H ou 24H).
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @param duree     durée sur laquelle on mesure les statistiques (1H ou 24H).
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String statsCapteurs(String idCapteur, int duree) throws RemoteException {
        return null;
    }

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param intervalle nouvel intervalle de mesure
     * @param idCapteur  chaine de caractère identifiant le capteur.
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String modifierIntervalle(int intervalle, String idCapteur) throws RemoteException {
        return null;
    }

    /**
     * Permet de modifier l'intervalle de mesure pour tous les capteurs
     *
     * @param intervalle nouvel intervalle de mesure
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String modifierIntervalleTous(int intervalle) throws RemoteException {

        return null;
    }

    /**
     * Permet d'enregistrer les informations dans la base de données contenant les valeurs relevées par les capteurs.
     * @param id         chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp       flottant représentant la température relevé par le capteur.
     * @param tauxH      flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException    si erreur lors de l'insertion dans la base de données.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void enregistrerValeur(String id, float temp, float tauxH, String horodatage) throws RemoteException, SQLException {

    }
}
