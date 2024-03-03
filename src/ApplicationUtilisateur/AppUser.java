package ApplicationUtilisateur;

import java.rmi.RemoteException;
import java.sql.SQLException;

public abstract class AppUser {

    /**
     * Permet à l'utilisateur d'ajouter/enregistrer un capteur
     * dans la base de donnée du Gestionnaire
     * @param idCapteur identifiant du capteur
     */
    protected static void ajouterCapteur(String idCapteur) {

    }

    /**
     * Permet de commencer l'enregistrement de relevé
     * @param idCapteur identifiant du capteur
     */
    protected static void demarrerCapteur(String idCapteur) {

    }

    /**
     * Permet d'arrêter l'enregistrement de relevé
     * @param idCapteur identifiant du capteur
     */
    protected static void stopperCapteur(String idCapteur) {

    }

    /**
     * Permet de retirer un capteur dans la base de données du gestionnaire contenant la liste des capteurs.
     * @param idCapteur identifiant du capteur
     */
    protected static void retirerCapteur(String idCapteur) {

    }

    /**
     * Permet de lister les capteurs enregistrés par l'utilisateur
     */
    protected static void listeCapteurs() throws SQLException, RemoteException {
        MainAppUser.leGestionnaire.listeCapteurs();
    }

    /**
     * Permet de visualiser le dernier relevé d'un capteur
     * @param idCapteur identifiant du capteur
     */
    protected void obtenirDernierReleve(String idCapteur) {

    }

    /**
     * Permet d'obtenir des statistiques sur les relevés d'un capteur (moyenne et tendances)
     * @param idCapteur identifiant du capteur
     * @param duree durée sur laquelle on mesure les statistiques (1H ou 24H).
     */
    protected static void statsCapteurs(String idCapteur, int duree) {

    }

    /**
     * Permet de modifier l'intervalle de mesure pour un capteur en particulier
     * @param intervalle nouvel intervalle de mesure
     * @param idCapteur identifiant du capteur
     */
    protected void modifierIntervalle(int intervalle, String idCapteur) {

    }

    /**
     * Permet de modifier l'intervalle de mesure pour tous les capteurs
     * @param intervalle nouvel intervalle de mesure
     */
    protected void modifierIntervalleTous(int intervalle) {

    }

}
