package Gestionnaire;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.sql.SQLException;

/**
 * Méthode de Gestionnaire accessible à distance
 */
public interface Gestionnaire extends Remote {

    /**
     * Permet d'ajouter un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur  chaine de caractère identifiant le capteur.
     * @throws SQLException
     * @throws RemoteException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String ajouterCapteur(String idCapteur) throws RemoteException, SQLException;


    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws RemoteException
     * @throws SQLException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String retirerCapteur(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de démarrer l'enregistrement des relevés pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws RemoteException
     * @throws SQLException 
     * @throws MalformedURLException
     * @throws NotBoundException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String demarrerCapteur(String idCapteur) throws RemoteException, SQLException, MalformedURLException, NotBoundException;

    /**
     * Permet de stopper l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws RemoteException 
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String stopperCapteur(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de lister les capteurs enregistrés par l'utilisateur
     * @throws SQLException 
     * @throws RemoteException 
     * @return une chaine de caractère représentant la liste des capteurs
     */
    public String listeCapteurs() throws RemoteException, SQLException;


    /**
     * Permet de visualiser le dernier relevé d'un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @throws RemoteException
     * @throws SQLException
     * @return une chaine de caractère représentant le dernier relevé du capteur
     */
    public String dernierReleve(String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée 1H.
     * @throws RemoteException 
     * @return une chaine de caractère contenant les stats
     */
    public String statsCapteursUneHeure() throws RemoteException, SQLException;

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée 24H.
     * @throws RemoteException 
     * @return une chaine de caractère contenant les stats
     */
    public String statsCapteursUneJournee() throws RemoteException, SQLException;

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @param intervalle nouvel intervalle de mesure
     * @throws RemoteException
     * @throws SQLException
     * @return une chaine de caractère contenant les stats
     */
    public String modifierIntervalle(int intervalle, String idCapteur) throws RemoteException, SQLException;

    /**
     * Permet de modifier l'intervalle de mesure pour tous les capteurs
     * @param intervalle nouvel intervalle de mesure
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException 
     */
    public String modifierIntervalleTous(int intervalle) throws RemoteException;

    /**
     * Permet d'enregistrer les informations dans la base de données contenant les valeurs relevées par les capteurs.
     * @param idCapteur  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp flottant représentant la température relevé par le capteur.
     * @param tauxH flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException
     * @throws RemoteException 
     */
    public void enregistrerValeur(String idCapteur, float temp, float tauxH, String horodatage) throws RemoteException, SQLException;

    /**
     * Indique le nombre de capteurs actif
     * @return le nombre de capteurs actif (c'est-à-dire en train d'effectuer des relevés)
     */
    public int nbCapteurActif() throws RemoteException;

    /**
     * Permet d'ajouter un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur  chaine de caractère identifiant l'actionneur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException
     * @throws RemoteException 
     */
    public String ajouterActionneur (String idActionneur) throws RemoteException, SQLException;

    /**
     * Ordonne à un actionneur de démarrer l'arrosage
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @param duree entier représentant la durée de l'arrosage (en MINUTE)
     * @throws RemoteException 
     * @throws SQLException
     * @throws MalformedURLException
     * @throws NotBoundException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String demarrerArrosage (String idActionneur, int duree) throws SQLException, MalformedURLException, NotBoundException, RemoteException;

    /**
     * Permet de retirer un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur chaine de caractère identifiant de l'actionneur.
     * @throws RemoteException 
     * @throws SQLException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String retirerActionneur (String idActionneur) throws RemoteException, SQLException;

    /**
     * Permet de connaître l'état de l'arrosage actuel.
     * Exemple de retour possible :
     *  - Arrosage en cours, fini dans ...
     *  - L'arrosage n'a jamais été effectué
     *  - Le dernier arrosage a été fait le ... à ... pendant ....
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @throws RemoteException 
     * @throws SQLException
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     */
    public String etatArrosage (String idActionneur) throws RemoteException, SQLException;

    /**
     * Notifie la fin de l'arrosage
     * @param idActionneur identifiant de l'actionneur qui vient de finir l'arrosage.
     * @param dureeDeLarrosage entier représentant la durée de l'arrosage (en MINUTE)
     * @throws RemoteException 
     */
    public void notificationFinArrosage(String idActionneur, int dureeDeLarrosage) throws RemoteException, SQLException;


    /**
     * Permet de lister les actionneurs enregistrés par l'utilisateur
     * @throws SQLException
     * @throws RemoteException 
     * @return une chaine de caractère représentant la liste des actionneurs
     */
    String listeActionneur() throws RemoteException, SQLException;
}
