package Gestionnaire;
import ApplicationUtilisateur.GestionnaireNotification;
import IoT.Actionneur;
import IoT.Capteur;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final Connection c;

    /**
     * Un gestionnaire est caractérisé par un nom
     */
    private final String nom;

    /**
     * Contient la liste des objets capteurs actifs (en train de faire des relevés)
     */
    private HashMap<String, Capteur> listeCapteursActif;

    /**
     * Contient la liste des objets actionneurs actifs (en train d'arroser)
     */
    private HashMap<String, Actionneur> listeActionneursActif;


    /**
     * Crée un objet GestionnaireImpl, cet objet implémente les méthodes de l'interface Gestionnaire
     * @throws RemoteException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public GestionnaireImpl() throws RemoteException, SQLException, ClassNotFoundException {
        super();
        this.c = connexion();
        nom = "Gestionnaire Agriconnect";
        listeCapteursActif = new HashMap<>();
        listeActionneursActif = new HashMap<>();
    }

    /**
     * @return les informations de l'actionneur sous la forme d'une chaine de caractères
     */
    @Override
    public String toString() {
        return "GestionnaireImpl{" +
                "nom='" + nom + '\'' +
                '}';
    }

    /**
     * Vérifie si le capteur existe et ajoute celui-ci dans la base de données 
     * du gestionnaire contenant la liste des capteurs.
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException   
     * @throws RemoteException
     */
    @Override
    public String ajouterCapteur(String idCapteur) throws RemoteException, SQLException {
        try {
            if (Outils.estCeQueLeCapteurEstEnregistre(idCapteur,c)) { // est-il deja dans la base ?
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
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException
     * @throws SQLException
     */
    @Override
    public String retirerCapteur(String idCapteur) throws RemoteException, SQLException {
        if (Outils.estCeQueLeCapteurEstEnregistre(idCapteur, c)) {
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
     * @throws RemoteException
     * @throws SQLException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    @Override
    public String demarrerCapteur(String idCapteur) throws RemoteException, SQLException, MalformedURLException, NotBoundException {
        if(Outils.estCeQueLeCapteurEstEnregistre(idCapteur,c)) {
            Capteur leCapteur = (Capteur) Naming.lookup("rmi://localhost:1099/" + idCapteur); // on récupère le capteur distant enregistré dans le registre RMI
            if (leCapteur.enFonction()) { // si le capteur effectue deja des relevés
                return "Le capteur effectue deja des relevés !";
            } // else, on démarre le capteur
            listeCapteursActif.put(idCapteur, leCapteur); // On enregistre le capteur dans la liste des capteurs actif.
            leCapteur.setGestionnaire((Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire")); // On affecte ce gestionnaire à la gestion du capteur via sa référence dans le registre RMI (et non pas avec this) pour que celui-ci soit accessible à distance
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
     * @throws RemoteException  
     */
    @Override
    public String stopperCapteur(String idCapteur) throws RemoteException, SQLException {
        if (Outils.estCeQueLeCapteurEstEnregistre(idCapteur,c)) {
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
     * @throws SQLException     
     * @throws RemoteException  
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
        if (resultat.isEmpty()) { // Aucune ligne retournée
            return "Aucun capteur n'a été enregistré pour le moment ...";
        }
        return resultat.toString();
    }

    /**
     * Permet de visualiser le dernier relevé d'un capteur
     * @param idCapteur chaine de caractère identifiant le capteur.
     * @return une chaine de caractère représentant le dernier relevé du capteur
     * @throws RemoteException  
     */
    @Override
    public String dernierReleve(String idCapteur) throws RemoteException, SQLException {
        String resultats = "";
        if (Outils.estCeQueLeCapteurEstEnregistre(idCapteur,c)) {
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
     * Permet d'obtenir des statistiques sur les relevés stockés en base de données (moyenne et tendances) pour une durée de 1H.
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException  
     */
    @Override
    public String statsCapteursUneHeure() throws RemoteException, SQLException {

        float moyenneUn, moyenneDeux;
        String resultat = ""; // Retour fait à l'utilisateur

        /* Obtention des données dans la base de données */
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.RELEVE_TEMPERATURE);
        instructions.setString(1, "-1 hour");
        ResultSet retourSQLTempUn = instructions.executeQuery(); // Retour de tous les relevés de température sur la dernière heure
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TEMPERATURE_INTERVALLE);
        instructions.setString(1, "-2 hour");
        instructions.setString(2, "-1 hour");
        ResultSet retourSQLTempDeux = instructions.executeQuery(); // Retour de tous les relevés de température compris entre 2 heures avant maintenant et 1 heure avant maintenant. → Pour le calcul de la tendance ...
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TAUXHUMIDITE);
        instructions.setString(1, "-1 hour");
        ResultSet retourSQLTauxHUn = instructions.executeQuery(); // Retour de tous les relevés de taux d'humidité sur la dernière heure
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TAUXHUMIDITE_INTERVALLE);
        instructions.setString(1, "-2 hour");
        instructions.setString(2, "-1 hour");
        ResultSet retourSQLTauxHDeux = instructions.executeQuery(); // Retour de tous les relevés de taux d'humidité compris entre 2 heures avant maintenant et 1 heure avant maintenant. → Pour le calcul de la tendance ...

        /* Calcul de la température moyenne sur h-1 et sur l'intervalle H-1 H-2 afin d'obtenir une tendance */
        moyenneUn = Outils.moyenne(retourSQLTempUn);
        moyenneDeux = Outils.moyenne(retourSQLTempDeux);
        resultat += !Float.isNaN(moyenneUn) ? "Moyenne de la température sur la dernière heure : " + moyenneUn + "\n" : "Pas de données de température disponibles sur cet intervalle.\n"; // Voir Outils.moyenne() pour plus d'info...
        /* Recherche de la tendance pour la température */
        if (Float.isNaN(moyenneDeux) || Float.isNaN(moyenneUn)) { // Peut-on calculer une tendance ?
            resultat += "Tendance indisponible pour le moment, il faut plus de relevés....\n";
        } else { // on compare la moyenne actuelle sur les relevés précédents afin d'obtenir une tendance
            if (moyenneUn > moyenneDeux) {
                resultat += "Tendance de la température : en HAUSSE\n";
            } else if (moyenneUn < moyenneDeux) {
                resultat += "Tendance de la température : en BAISSE\n";
            } else { // moyenneUn == moyenneDeux → possible, mais peu probable
                resultat += "La température moyenne est constante\n";
            }
        }

        /* Calcul du taux d'humidité moyen sur h-1 et sur l'intervalle H-1 H-2 afin d'obtenir une tendance */
        moyenneUn = Outils.moyenne(retourSQLTauxHUn);
        moyenneDeux = Outils.moyenne(retourSQLTauxHDeux);
        resultat += !Float.isNaN(moyenneUn) ? "Moyenne du taux d'humidité sur la dernière heure : " + moyenneUn + "\n" : "Pas de données sur le taux d'humidité disponibles sur cet intervalle.\n"; // Voir Outils.moyenne() pour plus d'info...
        /* Recherche de la tendance pour le taux d'humidité */
        if (Float.isNaN(moyenneDeux) || Float.isNaN(moyenneUn)) { // Peut-on calculer une tendance ?
            resultat += "Tendance indisponible pour le moment, il faut plus de relevés....\n";
        } else { // on compare la moyenne actuelle sur les relevés précédents afin d'obtenir une tendance
            if (moyenneUn > moyenneDeux) {
                resultat += "Tendance du taux d'humidité : en HAUSSE\n";
            } else if (moyenneUn < moyenneDeux) {
                resultat += "Tendance du taux d'humidité : en BAISSE\n";
            } else { // moyenneUn == moyenneDeux → possible, mais peu probable
                resultat += "Le taux d'humidité moyen est constant\n";
            }
        }

        return resultat;
    }

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances) pour une durée de 24H.
     * @throws RemoteException
     * @return une chaine de caractère contenant les stats
     */
    public String statsCapteursUneJournee() throws RemoteException, SQLException {
        float moyenneUn, moyenneDeux;
        String resultat = ""; // Retour fait à l'utilisateur

        /* Obtention des données dans la base de données */
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.RELEVE_TEMPERATURE);
        instructions.setString(1, "-24 hour");
        ResultSet retourSQLTempUn = instructions.executeQuery(); // Retour de tous les relevés de température sur les dernières 24 heures
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TEMPERATURE_INTERVALLE);
        instructions.setString(1, "-48 hour");
        instructions.setString(2, "-24 hour");
        ResultSet retourSQLTempDeux = instructions.executeQuery(); // Retour de tous les relevés de température compris entre 48 heures avant maintenant et 24 heures avant maintenant. → Pour le calcul de la tendance ...
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TAUXHUMIDITE);
        instructions.setString(1, "-24 hour");
        ResultSet retourSQLTauxHUn = instructions.executeQuery(); // Retour de tous les relevés de taux d'humidité sur les dernières 24 heures
        instructions = c.prepareStatement(RequeteSQL.RELEVE_TAUXHUMIDITE_INTERVALLE);
        instructions.setString(1, "-48 hour");
        instructions.setString(2, "-24 hour");
        ResultSet retourSQLTauxHDeux = instructions.executeQuery(); // Retour de tous les relevés de taux d'humidité compris entre 48 heures avant maintenant et 24 heures avant maintenant. → Pour le calcul de la tendance ...

        /* Calcul de la température moyenne sur h-24 et sur l'intervalle H-24 H-48 afin d'obtenir une tendance */
        moyenneUn = Outils.moyenne(retourSQLTempUn);
        moyenneDeux = Outils.moyenne(retourSQLTempDeux);
        resultat += !Float.isNaN(moyenneUn) ? "Moyenne de la température sur les dernières 24H : " + moyenneUn + "\n" : "Pas de données de température disponibles sur cet intervalle.\n"; // Voir Outils.moyenne() pour plus d'info...
        /* Recherche de la tendance pour la température */
        if (Float.isNaN(moyenneDeux) || Float.isNaN(moyenneUn)) { // Peut-on calculer une tendance ?
            resultat += "Tendance indisponible pour le moment, il faut plus de relevés....\n";
        } else { // on compare la moyenne actuelle sur les relevés précédents afin d'obtenir une tendance
            if (moyenneUn > moyenneDeux) {
                resultat += "Tendance de la température : en HAUSSE\n";
            } else if (moyenneUn < moyenneDeux) {
                resultat += "Tendance de la température : en BAISSE\n";
            } else { // moyenneUn == moyenneDeux → possible, mais peu probable
                resultat += "La température moyenne est constante\n";
            }
        }

        /* Calcul du taux d'humidité moyen sur h-1 et sur l'intervalle H-1 H-2 afin d'obtenir une tendance */
        moyenneUn = Outils.moyenne(retourSQLTauxHUn);
        moyenneDeux = Outils.moyenne(retourSQLTauxHDeux);
        resultat += !Float.isNaN(moyenneUn) ? "Moyenne du taux d'humidité sur la dernière heure : " + moyenneUn + "\n" : "Pas de données sur le taux d'humidité disponibles sur cet intervalle.\n"; // Voir Outils.moyenne() pour plus d'info...
        /* Recherche de la tendance pour le taux d'humidité */
        if (Float.isNaN(moyenneDeux) || Float.isNaN(moyenneUn)) { // Peut-on calculer une tendance ?
            resultat += "Tendance indisponible pour le moment, il faut plus de relevés....\n";
        } else { // on compare la moyenne actuelle sur les relevés précédents afin d'obtenir une tendance
            if (moyenneUn > moyenneDeux) {
                resultat += "Tendance du taux d'humidité : en HAUSSE\n";
            } else if (moyenneUn < moyenneDeux) {
                resultat += "Tendance du taux d'humidité : en BAISSE\n";
            } else { // moyenneUn == moyenneDeux → possible, mais peu probable
                resultat += "Le taux d'humidité moyen est constant\n";
            }
        }

        return resultat;
    }

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param intervalle nouvel intervalle de mesure
     * @param idCapteur  chaine de caractère identifiant le capteur.
     * @return une chaine de caractère contenant les stats
     * @throws RemoteException
     * @throws SQLException
     */
    @Override
    public String modifierIntervalle(int intervalle, String idCapteur) throws RemoteException, SQLException {
        if(Outils.estCeQueLeCapteurEstEnregistre(idCapteur,c)) {
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
     * @throws RemoteException  
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
     * @throws SQLException     
     * @throws RemoteException  
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
     * @return le nombre de capteurs actif (c'est-à-dire en train d'effectuer des relevés)
     */
    @Override
    public int nbCapteurActif() throws RemoteException {
        return listeCapteursActif.size();
    }

    /**
     * Permet d'ajouter un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws SQLException     
     * @throws RemoteException  
     */
    @Override
    public String ajouterActionneur(String idActionneur) throws RemoteException, SQLException {
        try {
            if (Outils.estCeQueLActionneurEstEnregistre(idActionneur,c)) { // est-il deja dans la base ?
                return "L'actionneur a deja été ajouté";
            } // else
            Actionneur lActionneur = (Actionneur) Naming.lookup("rmi://localhost:1099/" + idActionneur); // On essaye de récupérer un actionneur distant au sein du registre RMI, si pas d'exception alors l'actionneur recherché existe
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.INSERTION_ACTIONNEUR);
            instructions.setString(1, idActionneur); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setFloat(2, lActionneur.getGps()[0]);
            instructions.setFloat(3, lActionneur.getGps()[1]);
            instructions.executeUpdate();
            return "L'actionneur a été ajouté !";
        } catch (NotBoundException | ClassCastException | MalformedURLException err) {
            return "L'actionneur n'existe pas ou n'est pas actif";
        }
    }

    /**
     * Permet de retirer un actionneur dans la base de données du gestionnaire contenant la liste des actionneurs.
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException  
     * @throws SQLException    si erreur lors de la recherche dans la base de données.
     */
    @Override
    public String retirerActionneur(String idActionneur) throws RemoteException, SQLException {
        if (Outils.estCeQueLActionneurEstEnregistre(idActionneur,c)) { // l'actionneur est enregistré.
            if(listeActionneursActif.containsKey(idActionneur)) {
                return "Impossible de retirer l'actionneur pour l'instant car l'arrosage n'est pas terminé ....\n"
                        +etatArrosage(idActionneur);
            } // else
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.SUPPRESSION_ACTIONNEUR); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setString(1, idActionneur);
            instructions.executeUpdate();
            return "L'actionneur a bien été retiré !";
        } else {
            return "Erreur, l'actionneur n'est pas enregistré !";
        }
    }

    /**
     * Ordonne à un actionneur de démarrer l'arrosage
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @param duree        entier représentant la durée de l'arrosage (en MINUTE)
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException        
     * @throws SQLException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    @Override
    public String demarrerArrosage(String idActionneur, int duree) throws SQLException, MalformedURLException, NotBoundException, RemoteException {
        if(Outils.estCeQueLActionneurEstEnregistre(idActionneur,c)) {
            Actionneur lActionneur = (Actionneur) Naming.lookup("rmi://localhost:1099/" + idActionneur); // on récupère l'actionneur distant enregistré dans le registre RMI
            if (lActionneur.enFonction()) { // si l'arrosage est deja en cours
                return etatArrosage(idActionneur);
            } // else, on démarre l'arrosage
            lActionneur.setGestionnaire((Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire")); // On affecte ce gestionnaire à la gestion du capteur via sa référence dans le registre RMI (et non pas avec this) pour que celui-ci soit accessible à distance
            listeActionneursActif.put(idActionneur, lActionneur); // On enregistre l'actionneur dans la liste des actionneurs actif. (Arrosage en cours)
            lActionneur.declencherArrosage(duree);
            return "L'arrosage vient de débuter pour une durée de " + duree + " minute";
        } else {
            return "Erreur, l'actionneur n'est pas enregistré !";
        } 
    }

    /**
     * Permet de connaître l'état de l'arrosage actuel.
     * Exemple de retour possible :
     * - L'arrosage fini dans ...
     * - L'arrosage n'a jamais été effectué
     * - Le dernier arrosage a été fait le ... à ... pendant ....
     * @param idActionneur chaine de caractère identifiant l'actionneur.
     * @return une chaine de caractère spécifiant si l'opération a réussi ou non
     * @throws RemoteException  
     * @throws SQLException
     */
    @Override
    public String etatArrosage(String idActionneur) throws RemoteException, SQLException {
        if (Outils.estCeQueLActionneurEstEnregistre(idActionneur, c)) { // l'actionneur est enregistré.
            if(listeActionneursActif.containsKey(idActionneur)) {
                int tempsRestant = listeActionneursActif.get(idActionneur).obtenirTempsRestantArrosage();
                String aRetourner =  tempsRestant < 60 ? tempsRestant + " secondes" : (tempsRestant/60) + " minutes et " + (tempsRestant%60) + " secondes";
                return "L'arrosage fini dans " + aRetourner;
            } // else
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.ETAT_ARROSAGE); // On utilise PreparedStatement pour éviter les injections SQL
            instructions.setString(1, idActionneur);
            ResultSet retourSQL = instructions.executeQuery();
            if (retourSQL.next()) {
                String dateHeure = retourSQL.getString("DernierArrosage");
                if(dateHeure != null) {
                    String[] split = dateHeure.split(" "); // split[0] => Date split[1] => Heure
                    return "Le dernier arrosage a été fait le " + split[0] + " à " + split[1] + " pendant " + retourSQL.getInt("Duree")/60 + " minute(s)"; // on récupère la valeur en seconde, on la convertie en minute
                } else {
                    return "L'arrosage n'a jamais été effectué";
                }
            } // else ... on est sûr de recevoir une ligne puisque l'on vérifie avant que l'actionneur est enregistré.
            return "";
        } else {
            return "Erreur, l'actionneur n'est pas enregistré !";
        }
    }

    /**
     * Notifie la fin de l'arrosage
     * @param idActionneur identifiant de l'actionneur qui vient de finir l'arrosage.
     * @param dureeDeLarrosage entier représentant la durée de l'arrosage (en MINUTE)
     * @throws RemoteException  
     */
    @Override
    public void notificationFinArrosage(String idActionneur, int dureeDeLarrosage) throws RemoteException, SQLException {
        try {
            GestionnaireNotification gestionnaireNotif = (GestionnaireNotification) Naming.lookup("rmi://localhost:1099/notification"); // On essaye de récupérer le gestionnaire de notification distant au sein du registre RMI, si pas d'exception alors l'objet recherché existe
            gestionnaireNotif.notification("----------------------------------\n"
                                             + "Information de l'actionneur "
                                             + idActionneur
                                             + "\nL'arrosage est terminé\n"
                                             + "----------------------------------"
                                          ); // On informe l'application de l'utilisateur
        } catch (NotBoundException | ClassCastException | MalformedURLException err) {
            System.out.println("Impossible de notifier l'application cliente\n");
        } finally {
            listeActionneursActif.remove(idActionneur); // comme l'arrosage est terminé, on retire l'actionneur de la liste des actionneurs en train d'arroser.
            PreparedStatement instructions = c.prepareStatement(RequeteSQL.INSERTION_ETAT_ARROSAGE); // On utilise PreparedStatement pour éviter les injections SQL
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // On a défini le format de l'horodatage
            String horodatage = LocalDateTime.now().format(format); // On récupère l'horodatage actuel, on lui applique le format
            instructions.setString(1, horodatage);
            instructions.setInt(2, dureeDeLarrosage*60); // on stocke en seconde dans la BD
            instructions.setString(3,idActionneur);
            instructions.executeUpdate();
        }
    }

    /**
     * Permet de lister les actionneurs enregistrés par l'utilisateur
     * @return une chaine de caractère représentant la liste des actionneurs
     * @throws SQLException     
     * @throws RemoteException  
     */
    @Override
    public String listeActionneur() throws RemoteException, SQLException {
        Statement instructions = c.createStatement();
        ResultSet retourSQL = instructions.executeQuery(RequeteSQL.SELECT_ACTIONNEUR);
        StringBuilder resultat = new StringBuilder();
        while (retourSQL.next()) {
            resultat.append(retourSQL.getString("Id")).append(" ");
            resultat.append(retourSQL.getFloat("Latitude")).append(" ");
            resultat.append(retourSQL.getFloat("Longitude")).append("\n");
        }
        if (resultat.isEmpty()) { // Aucune ligne retournée
            return "Aucun actionneur n'a été enregistré pour le moment ...";
        } // else
        return resultat.toString();
    }
}
