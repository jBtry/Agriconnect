package ApplicationUtilisateur;

public abstract class AppUser {

    /**
     * Permet à l'utilisateur d'ajouter/enregistrer un capteur
     * dans la base de donnée du Gestionnaire
     */
    public void ajouterCapteur(String idCapteur) {

    }

    /**
     * Permet de commencer l'enregistrement de relevé
     * @param id
     */
    public void demarrerCapteur(String id) {

    }

    /**
     * Permet d'arrêter l'enregistrement de relevé
     * @param id
     */
    public void stopperCapteur(String id) {

    }

    /**
     *
     * @param idCapteur
     */
    public void retirerCapteur(String idCapteur) {

    }

    /**
     *
     */
    public void listeCapteurs() {

    }

    /**
     *
     * @param idCapteur
     */
    public void obtenirDernierReleve(String idCapteur) {

    }

    /**
     *
     * @param idCapteur
     * @param duree
     */
    public void statsCapteurs(String idCapteur, int duree) {

    }

    /**
     *
     * @param intervalle
     * @param idCapteur
     */
    public void modifierIntervalle(int intervalle, String idCapteur) {

    }

}
