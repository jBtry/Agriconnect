package Gestionnaire;
import IoT.Capteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;

import static Gestionnaire.ConnexionBD.connexion;

/**
 * Gère une liste des capteurs associés et enregistre leurs relevés
 */
public class GestionnaireImpl extends UnicastRemoteObject implements Gestionnaire {

    /**
     * Contient la connexion/session avec la base de données afin d'effectuer des opérations sur celle-ci
     */
    private Connection c;

    /**
     * Un gestionnaire est caractérisé par un nom
     */
    private String nom;

    private HashMap<String, Capteur> listeCapteursActif;


    public GestionnaireImpl() throws RemoteException, SQLException, ClassNotFoundException {
        super();
        this.c = connexion();
        nom = "Gestionnaire Agriconnect";
        listeCapteursActif = new HashMap<>();
    }

    @Override
    public String toString() {
        return "GestionnaireImpl{" +
                "nom='" + nom + '\'' +
                '}';
    }

    /**
     * Vérifie si le capteur existe
     * et ajoute celui-ci dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException    si erreur lors de l'insertion dans la base de données.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String ajouterCapteur(String idCapteur) throws RemoteException, SQLException {
        try {
            if (estCeQueLeCapteurEstEnregistre(idCapteur)) { // est-il deja dans la base ?
                return "Le capteur a deja été ajouté";
            } // else
            Capteur leCapteur = (Capteur) Naming.lookup("rmi://localhost:1099/" + idCapteur); // On essaye de récupérer un capteur distant au sein du registre RMI, si pas d'exception alors le capteur recherché existe
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.INSERTION_CAPTEUR);
            instructions.setString(1, idCapteur); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setFloat(2, leCapteur.getGps()[0]);
            instructions.setFloat(3, leCapteur.getGps()[1]);
            instructions.executeUpdate();
            return "Le capteur a été ajouté !";
        } catch (NotBoundException | ClassCastException | MalformedURLException err) {
            return "Le capteur n'existe pas ou n'est pas actif";
        }
    }

    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     *
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String retirerCapteur(String idCapteur) throws RemoteException, SQLException {
        if (estCeQueLeCapteurEstEnregistre(idCapteur)) {
            if (listeCapteursActif.containsKey(idCapteur)) { // le capteur est enregistré et actif
                stopperCapteur(idCapteur);
            } // else
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.SUPPRESSION_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setString(1, idCapteur);
            instructions.executeUpdate();
            return "Le capteur a bien été retiré !";
        } else {
            return "Erreur, le capteur n'est pas enregistré !";
        }
    }

    /**
     * Permet de démarrer l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String demarrerCapteur(String idCapteur) throws RemoteException, SQLException, MalformedURLException, NotBoundException {
        if(estCeQueLeCapteurEstEnregistre(idCapteur)) {
            Capteur leCapteur = (Capteur) Naming.lookup("rmi://localhost:1099/" + idCapteur); // on récupère le capteur distant enregistré dans le registre RMI
            if (leCapteur.enFonction()) { // si le capteur effectue deja des relevés
                return "Le capteur effectue deja des relevés !";
            } // else, on démarre le capteur
            listeCapteursActif.put(idCapteur, leCapteur); // On enregistre le capteur dans la liste des capteurs actif.
            leCapteur.setGestionnaire(this); // On affecte ce gestionnaire à la gestion du capteur
            leCapteur.demarrerEnregistrementReleve();
            return "Le capteur a bien été démarré !";
        } else {
            return "Erreur, le capteur n'est pas enregistré !";
        }
    }

    /**
     * Permet de stopper l'enregistrement de relevé pour un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String stopperCapteur(String idCapteur) throws RemoteException, SQLException {
        if (estCeQueLeCapteurEstEnregistre(idCapteur)) {
            if (listeCapteursActif.containsKey(idCapteur)) { // le capteur est enregistré et actif
                Capteur leCapteur = listeCapteursActif.get(idCapteur);
                leCapteur.onOff(); // on passe actif à false.
                listeCapteursActif.remove(idCapteur); // on retire le capteur de la liste des capteurs actifs
                return "Le capteur a bien été stoppé !";
            } else {
                return "Le capteur n'est pas actif !";
            }
        } else {
            return "Erreur, le capteur n'est pas enregistré !";
        }
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
        while (retourSQL.next()) {
            resultat.append(retourSQL.getString("Id")).append(" ");
            resultat.append(retourSQL.getFloat("Latitude")).append(" ");
            resultat.append(retourSQL.getFloat("Longitude")).append("\n");
        }
        if (resultat.length() == 0) { // Aucune ligne retournée
            return "Aucun capteur n'a été enregistré pour le moment ...";
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
    public String dernierReleve(String idCapteur) throws RemoteException, SQLException {
        String resultats = "";
        if (estCeQueLeCapteurEstEnregistre(idCapteur)) {
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.DERNIERE_INFO_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setString(1, idCapteur);
            ResultSet retourSQL = instructions.executeQuery();
            resultats+=retourSQL.getFloat("Temperature")+" | ";
            resultats+=retourSQL.getFloat("TauxHumidite")+" | ";
            resultats+=retourSQL.getString("Horodatage");
        } else {
            resultats = "Erreur, le capteur n'est pas enregistré !";
        }
        return resultats;
    }

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée (1H ou 24H).
     *
     * @param duree durée sur laquelle on mesure les statistiques (1H ou 24H).
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String statsCapteurs(int duree) throws RemoteException, SQLException {
        float sommeTemperature = 0;
        float nombreTemperature = 0;
        float moyenneTemperature = 0;
        float derniereTemperature = Float.MIN_VALUE; // Utilisé pour calculer la hausseTemperature
        float baisseTemperature = 0;
        float hausseTemperature = 0;

        float sommeTauxHumidite = 0;
        float nombreTauxHumidite = 0;
        float moyenneTauxHumidite = 0;
        float dernierTauxHumidite = Float.MIN_VALUE; // Utilisé pour calculer la hausseTauxHumidite
        float baisseTauxHumidite = 0;
        float hausseTauxHumidite = 0;

        String tendance = "";
        String resultat = "";

        if (duree == 1) { // Sur une heure
            Statement instructions = c.createStatement();
            ResultSet retourSQL = instructions.executeQuery(RequeteSQL.RELEVE_TEMPERATURE_1H); // Retour de tous les relevés de température

            while (retourSQL.next()) {
                float TemperatureActuelle = retourSQL.getFloat(1);
                sommeTemperature += TemperatureActuelle;
                nombreTemperature++;
                if (derniereTemperature != Float.MIN_VALUE && TemperatureActuelle > derniereTemperature) {
                    hausseTemperature++;
                }
                if (derniereTemperature != Float.MIN_VALUE && TemperatureActuelle < derniereTemperature) {
                    baisseTemperature++;
                }
                derniereTemperature = TemperatureActuelle;
            }

            if (nombreTemperature > 0) { // calcul de la moyenne de la température
                moyenneTemperature = sommeTemperature / nombreTemperature;
                resultat += "Moyenne de la température : " + moyenneTemperature + "\n";

            } else {
                resultat += "Pas de données de température disponibles sur cet intervalle." + "\n";
            }

            if (hausseTemperature - baisseTemperature > 0) { // Vérification de la tendance de la température
                tendance = "Hausse";
                resultat += "Tendance de la température : " + tendance + "\n";
            } else if (hausseTemperature - baisseTemperature < 0) {
                tendance = "Baisse";
                resultat += "Tendance de la température : " + tendance + "\n";
            } else resultat += "Il n'y a pas de tendance" + "\n"; // possible si deux valeurs générées successivement sont égales (peu probable)


            retourSQL = instructions.executeQuery(RequeteSQL.RELEVE_TAUXHUMIDITE_1H); // Retour de tous les relevés du taux d'humidité
            while (retourSQL.next()) {
                float TauxHumiditeActuel = retourSQL.getFloat(1);
                sommeTauxHumidite += TauxHumiditeActuel;
                nombreTauxHumidite++;
                if (dernierTauxHumidite != Float.MIN_VALUE && TauxHumiditeActuel > dernierTauxHumidite) {
                    hausseTauxHumidite++;
                }
                if (dernierTauxHumidite != Float.MIN_VALUE && TauxHumiditeActuel < dernierTauxHumidite) {
                    baisseTauxHumidite++;
                }
                dernierTauxHumidite = TauxHumiditeActuel;
            }
            if (nombreTemperature > 0) { // calcul de la moyenne du taux d'humidité
                moyenneTauxHumidite = sommeTauxHumidite / nombreTauxHumidite;
                resultat += "Moyenne du taux d'humidité : " + moyenneTauxHumidite + "\n";
            } else {
                resultat += "Pas de données du taux d'humidité disponibles sur cet intervalle." + "\n";
            }

            if (hausseTauxHumidite - baisseTauxHumidite > 0) { // Vérification de la tendance du taux d'humidité
                tendance = "Hausse";
                resultat += "Tendance du taux humidite : " + tendance + "\n";
            } else if (hausseTauxHumidite - baisseTauxHumidite < 0) {
                tendance = "Baisse";
                resultat += "Tendance du taux humidite : " + tendance + "\n";
            } else resultat += "Il n'y a pas de tendance" + "\n";

            return resultat;

        } else if (duree == 24) { // Sur 24 heures
            Statement instructions = c.createStatement();
            ResultSet retourSQL = instructions.executeQuery(RequeteSQL.RELEVE_TEMPERATURE_24H); // Retour de tous les relevés de température

            while (retourSQL.next()) {
                float TemperatureActuelle = retourSQL.getFloat(1);
                sommeTemperature += TemperatureActuelle;
                nombreTemperature++;
                if (derniereTemperature != Float.MIN_VALUE && TemperatureActuelle > derniereTemperature) {
                    hausseTemperature++;
                }
                if (derniereTemperature != Float.MIN_VALUE && TemperatureActuelle < derniereTemperature) {
                    baisseTemperature++;
                }
                derniereTemperature = TemperatureActuelle;
            }

            if (nombreTemperature > 0) {// calcul de la moyenne de la température
                moyenneTemperature = sommeTemperature / nombreTemperature;
                resultat += "Moyenne de la température : " + moyenneTemperature + "\n";

            } else {
                resultat += "Pas de données de température disponibles sur cet intervalle." + "\n";
            }

            if (hausseTemperature - baisseTemperature > 0) { // Vérification de la tendance de la température
                tendance = "Hausse";
                resultat += "Tendance de la température : " + tendance + "\n";
            } else if (hausseTemperature - baisseTemperature < 0) {
                tendance = "Baisse";
                resultat += "Tendance de la température : " + tendance + "\n";
            } else resultat += "Il n'y a pas de tendance" + "\n";


            retourSQL = instructions.executeQuery(RequeteSQL.RELEVE_TAUXHUMIDITE_24H); // Retour de tous les relevés du taux d'humidité
            while (retourSQL.next()) {
                float TauxHumiditeActuel = retourSQL.getFloat(1);
                sommeTauxHumidite += TauxHumiditeActuel;
                nombreTauxHumidite++;
                if (dernierTauxHumidite != Float.MIN_VALUE && TauxHumiditeActuel > dernierTauxHumidite) {
                    hausseTauxHumidite++;
                }
                if (dernierTauxHumidite != Float.MIN_VALUE && TauxHumiditeActuel < dernierTauxHumidite) {
                    baisseTauxHumidite++;
                }
                dernierTauxHumidite = TauxHumiditeActuel;
            }
            if (nombreTemperature > 0) { // calcul de la moyenne du taux d'humidité
                moyenneTauxHumidite = sommeTauxHumidite / nombreTauxHumidite;
                resultat += "Moyenne du taux d'humidité : " + moyenneTauxHumidite + "\n";
            } else {
                resultat += "Pas de données du taux d'humidité disponibles sur cet intervalle." + "\n";
            }

            if (hausseTauxHumidite - baisseTauxHumidite > 0) { // Vérification de la tendance du taux d'humidité
                tendance = "Hausse";
                resultat += "Tendance du taux humidite : " + tendance;
            } else if (hausseTauxHumidite - baisseTauxHumidite < 0) {
                tendance = "Baisse";
                resultat += "Tendance du taux humidite : " + tendance;
            } else resultat += "Il n'y a pas de tendance";

            return resultat;
        }
        return resultat;
    }

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param intervalle nouvel intervalle de mesure
     * @param idCapteur  chaine de caractère identifiant le capteur.
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String modifierIntervalle(int intervalle, String idCapteur) throws RemoteException, SQLException {
        if(estCeQueLeCapteurEstEnregistre(idCapteur)) {
            listeCapteursActif.get(idCapteur).modifierIntervalle(intervalle);
            return "L'intervalle de mesure du capteur : " + idCapteur + " est maintenant de " + intervalle + " secondes";
        } else {
            return "Erreur, le capteur n'est pas enregistré !";
        }
    }

    /**
     * Permet de modifier l'intervalle de mesure pour tous les capteurs
     * @param intervalle nouvel intervalle de mesure
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public String modifierIntervalleTous(int intervalle) throws RemoteException {
        Collection<Capteur> capteurs = listeCapteursActif.values();
        for (Capteur leCapteur : capteurs) {
            leCapteur.modifierIntervalle(intervalle);
        }
        return "L'intervalle de mesure de tous les capteurs est maintenant de : " + intervalle + " secondes";
    }

    /**
     * Permet d'enregistrer les informations dans la base de données contenant les valeurs relevées par les capteurs.
     * @param idCapteur  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp       flottant représentant la température relevé par le capteur.
     * @param tauxH      flottant représentant le taux d'humidité relevé par le capteur.
     * @param horodatage date et heure du relevé
     * @throws SQLException    si erreur lors de l'insertion dans la base de données.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void enregistrerValeur(String idCapteur, float temp, float tauxH, String horodatage) throws RemoteException, SQLException {
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.INSERTION_RELEVE); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, idCapteur);
        instructions.setFloat(2,temp);
        instructions.setFloat(3,tauxH);
        instructions.setString(4, horodatage);
        instructions.executeUpdate();
    }

    /**
     * Permet de vérifier si un capteur est enregistré dans la BD.
     * Si le capteur n'est pas enregistré dans la BD, on n'enregistre pas ses relevés.
     * On le considère comme inconnu
     */
    private boolean estCeQueLeCapteurEstEnregistre(String idCapteur) throws SQLException {
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.EXISTENCE_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, idCapteur);
        ResultSet retourSQL = instructions.executeQuery();
        return retourSQL.next() ? true : false;  // Vérifier si le capteur existe (retourSQL.next() retourne True si au moins une ligne est retournée)
    }

    /**
     * @return le nombre de capteurs actif (c'est-à-dire en train d'effectuer des relevés)
     */
    @Override
    public int nbCapteurActif() throws RemoteException {
        return listeCapteursActif.size();
    }
}
