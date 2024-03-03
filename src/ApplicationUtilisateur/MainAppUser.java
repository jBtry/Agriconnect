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

    /** Pour la saisie utilisateur */
    private static final Scanner clavier = new Scanner(System.in);

    /** Le gestionnaire */
    protected static Gestionnaire leGestionnaire;

    /* Demande un choix à l'utilisateur */
    private static int demanderChoix() {
        MainAppUser.afficher(Textes.CHOIX);
        int choix = 0;
        do {
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISI);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
            }
        } while (choix == 0);

        return choix;
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
            int choix;
            MainAppUser.afficher(Textes.ACCUEIL);
            while (true) {
                MainAppUser.afficher(Textes.FCT);
                choix = MainAppUser.demanderChoix();
                switch (choix) {
                    case 1 -> ; // 1 - Ajouter un capteur
                    case 2 -> ; // 2 - Démarrer un capteur
                    case 3 -> ; // 3 - Stopper un capteur
                    case 4 -> ; // 4 - Retirer un capteur
                    case 5 -> AppUser.listeCapteurs(); ; // 5 - Lister les capteurs
                    case 6 -> ; // 6 - Voir le dernier relevé d'un capteur
                    case 7 -> ; // 7 - Obtenir des statistiques sur les relevés (moyenne et tendances)
                    case 8 -> ; // 8 - Modifier l'intervalle de mesure pour un capteur
                    case 9 -> ; // 9 - Modifier l'intervalle de mesure pour tous les capteurs
                    case 10 -> System.exit(0); // 10 - Quitter Agriconnect
                    default -> MainAppUser.afficher(Textes.ERR_SAISI); // Erreur de saisie
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
