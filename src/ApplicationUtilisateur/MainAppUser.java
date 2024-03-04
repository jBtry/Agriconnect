package ApplicationUtilisateur;

import Gestionnaire.Gestionnaire;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class MainAppUser {
    /**
     * Application de l'utilisateur
     * Met à disposition les fonctionnalités suivantes :
     * - Ajouter un capteur
     * - Démarrer un capteur
     * - Stopper un capteur
     * - Retirer un capteur
     * - Lister les capteurs
     * - Voir le dernier relevé d'un capteur
     * - Obtenir des statistiques sur les relevés (moyenne et tendances)
     * - Modifier l'intervalle de mesure pour un ou bien tous les capteurs
     * @param args non utilisés
     */

    //TODO : modifier diag classe

    /** Le gestionnaire */
    protected static Gestionnaire leGestionnaire;

    /* Demande un choix à l'utilisateur */
    private static int demanderChoix() {
        Scanner clavier = new Scanner(System.in);
        int choix = 0;
        do {
            MainAppUser.afficher(Textes.CHOIX);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISI_FCT);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
            }
        } while (choix == 0);
        return choix;
    }

    /* Demande de saisir l'identifiant du capteur */
    private static String demanderCapteur() {
        Scanner clavier = new Scanner(System.in);
        MainAppUser.afficher(Textes.CAPTEUR);
        return clavier.nextLine();
    }

    /* Demande de saisir le nouvel intervalle de mesure */
    private static int demanderIntervalle() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            MainAppUser.afficher(Textes.INTERVALLE);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISI_INTERVALLE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix == 0);
        return choix;
    }

    /* Demande de saisir la durée sur laquelle obtenir les statistiques */
    private static int demanderDuree() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            MainAppUser.afficher(Textes.DUREE);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISI_DUREE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix < 1 | choix > 2);
        return choix == 1 ? 24 : 1; // 24h ou 1h ?
    }


    /* Affiche les menus de l'application */
    private static void afficher(String texte) {
        System.out.println(texte + "\n");

    }

    /* -----------MAIN----------- */
    public static void main(String args[]) {
        /* Récupération de l'objet gestionnaire distant */
        try {
            leGestionnaire = (Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire");
            int choix, intervalle, duree;
            String idCapteur;
            MainAppUser.afficher(Textes.ACCUEIL);
            while (true) {
                MainAppUser.afficher(Textes.FCT);
                choix = MainAppUser.demanderChoix();
                switch (choix) {
                    case 1 -> { // 1 - Ajouter un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.ajouterCapteur(idCapteur));
                    }

                    case 2 -> { // 2 - Démarrer un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.demarrerCapteur(idCapteur));
                    }

                    case 3 -> { // 3 - Stopper un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.stopperCapteur(idCapteur));
                    }

                    case 4 ->  { // 4 - Retirer un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.retirerCapteur(idCapteur));
                    }

                    case 5 -> MainAppUser.afficher(leGestionnaire.listeCapteurs()); // 5 - Lister les capteurs

                    case 6 -> { // 6 - Voir le dernier relevé d'un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.dernierReleve(idCapteur));
                    }

                    case 7 -> { // 7 - Obtenir des statistiques sur les relevés (moyenne et tendances)
                        duree = MainAppUser.demanderDuree();
                        idCapteur = MainAppUser.demanderCapteur();
                        MainAppUser.afficher(leGestionnaire.statsCapteurs(idCapteur, duree));
                    }

                    case 8 -> { // 8 - Modifier l'intervalle de mesure pour un capteur
                        idCapteur = MainAppUser.demanderCapteur();
                        intervalle = MainAppUser.demanderIntervalle();
                        MainAppUser.afficher(leGestionnaire.modifierIntervalle(intervalle, idCapteur));
                    }
                    case 9 -> { // 9 - Modifier l'intervalle de mesure pour tous les capteurs
                        intervalle = MainAppUser.demanderIntervalle();
                        MainAppUser.afficher(leGestionnaire.modifierIntervalleTous(intervalle));
                    }

                    case 10 -> { // 10 - Quitter Agriconnect
                        MainAppUser.afficher(Textes.ABIENTOT);
                        System.exit(0);
                    }

                    default -> MainAppUser.afficher(Textes.ERR_SAISI_FCT); // Erreur de saisie
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
