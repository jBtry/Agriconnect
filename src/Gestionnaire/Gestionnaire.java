package Gestionnaire;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Méthode accessible à distance pour un Gestionnaire
 */
public interface Gestionnaire extends java.rmi.Remote {

    /**
     * Permet d'ajouter un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur  chaine de caractère identifiant le capteur.
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String ajouterCapteur(String idCapteur) throws RemoteException, SQLException;


    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @throws SQLException si erreur lors de la suppression du capteur dans la base de données
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String retirerCapteur(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de démarrer l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     * @throws MalformedURLException si l'objet distant est introuvable
     * @throws NotBoundException si l'objet distant est introuvable
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String demarrerCapteur(String idCapteur) throws RemoteException, SQLException, MalformedURLException, NotBoundException;

    /**
     * Permet de stopper l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String stopperCapteur(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de lister les capteurs enregistrés par l'utilisateur
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère représentant la liste des capteurs
     */
    public String listeCapteurs() throws RemoteException, SQLException;


    /**
     * Permet de visualiser le dernier relevé d'un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère représentant le dernier relevé du capteur
     */
    public String dernierReleve(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée 1H.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère contenant les stats
     */
    public String statsCapteursUneHeure() throws RemoteException, SQLException;

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée 24H.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère contenant les stats
     */
    public String statsCapteursUneJournee() throws RemoteException, SQLException;

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @param intervalle nouvel intervalle de mesure
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @return une chaine de caractère contenant les stats
     */
    public String modifierIntervalle(int intervalle, String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de modifier l'intervalle de mesure pour tous les capteurs
     * @param intervalle nouvel intervalle de mesure
     * @return une chaine de caractère contenant les stats
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public String modifierIntervalleTous(int intervalle) throws RemoteException;

    /**
     * Permet d'enregistrer les informations dans la base de données contenant les valeurs relevées par les capteurs.
     * @param idCapteur  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp flottant représentant la température relevé par le capteur.
     * @param tauxH flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void enregistrerValeur(String idCapteur, float temp, float tauxH, String horodatage) throws RemoteException, SQLException;

    /**
     * @return le nombre de capteur actif (c'est-à-dire en train d'effectuer des relevés)
     */
    public int nbCapteurActif() throws RemoteException;

    /**
     * Permet d'ajouter un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur  chaine de caractère identifiant l'actionneur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException si erreur lors de l'insertion dans la base de données.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public String ajouterActionneur (String idActionneur) throws RemoteException, SQLException;

    /**
     * Ordonne à un actionneur de démarrer l'arrosage
     * @param idActionneur chaine de caractère identifiant le capteur.
     * @param duree entier représentant la durée de l'arrosage (en MINUTE)
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     * @throws MalformedURLException si l'objet distant est introuvable
     * @throws NotBoundException si l'objet distant est introuvable
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String demarrerArrosage (String idActionneur, int duree) throws SQLException, MalformedURLException, NotBoundException, RemoteException;;

    /**
     * Permet de retirer un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String retirerActionneur (String idActionneur) throws RemoteException, SQLException;

    /**
     * Permet de connaître l'état de l'arrosage actuel.
     * Exemple de retour possible :
     *  - Arrosage en cours, fini dans ...
     *  - L'arrosage n'a jamais été effectué
     *  - Le dernier arrosage a été fait le ... à ... pendant ....
     * @param idActionneur chaine de caractère identifiant le capteur.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String etatArrosage (String idActionneur) throws RemoteException, SQLException;

    /**
     * Notifie la fin de l'arrosage
     * @param idActionneur identifiant de l'actionneur qui vient de finir l'arrosage.
     * @param dureeDeLarrosage entier représentant la durée de l'arrosage (en MINUTE)
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void notificationFinArrosage(String idActionneur, int dureeDeLarrosage) throws RemoteException, SQLException;



}
