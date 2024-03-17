package ApplicationUtilisateur;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contient les méthodes utilisées par
 * la méthode main de la classe MainAppUser
 * pour afficher des messages et demander
 * la saisie de diverses informations à l'utilisateur.
 */
public abstract class Outils {

    /** Permet de demander un choix à l'utilisateur */
    protected static int demanderChoix() {
        Scanner clavier = new Scanner(System.in);
        int choix = 0;
        do {
            afficher(Textes.CHOIX);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                afficher(Textes.ERR_SAISIE_FCT);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
            }
        } while (choix == 0);
        return choix;
    }

    /** Permet de demander la saisie de l'identifiant d'un capteur */
    protected static String demanderCapteur() {
        Scanner clavier = new Scanner(System.in);
        afficher(Textes.CAPTEUR);
        return clavier.nextLine();
    }

    /** Permet de demander la saisie de l'identifiant d'un actionneur */
    protected static String demanderActionneur() {
        Scanner clavier = new Scanner(System.in);
        afficher(Textes.ACTIONNEUR);
        return clavier.nextLine();
    }


    /** Permet de demander la saisie du nouvel intervalle de mesure */
    protected static int demanderIntervalle() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            afficher(Textes.INTERVALLE);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                afficher(Textes.ERR_SAISIE_INTERVALLE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix == 0);
        return choix;
    }

    /** Permet de demander la saisie de la durée sur laquelle obtenir les statistiques */
    protected static int demanderDureeStat() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            afficher(Textes.DUREE_STATS);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                afficher(Textes.ERR_SAISIE_DUREE_STATS);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix < 1 | choix > 2);
        return choix == 1 ? 24 : 1; // 24h ou 1h ?
    }

    /** Permet de demander la saisie de la durée de l'arrosage */
    protected static int demanderDureeArrosage() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            afficher(Textes.DUREE_ARROSAGE);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                afficher(Textes.ERR_SAISIE_DUREE_ARROSAGE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix == 0);
        return choix;
    }

    /** Affiche les menus de l'application **/
    protected static void afficher(String texte) {
        System.out.println(texte + "\n");
    }

}
