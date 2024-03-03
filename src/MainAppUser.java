import ApplicationUtilisateur.Textes;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainAppUser {
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

    /* Pour la saisie utilisateur */
    private static final Scanner clavier = new Scanner(System.in);
    public static void main(String args[]) {
        int choix;
        MainAppUser.afficher(Textes.ACCUEIL);
        while(true) {
            MainAppUser.afficher(Textes.FCT);
            choix = MainAppUser.demanderChoix();
            switch (choix) {
                case 1 -> ;
                case 2 -> ;
                case 3 -> ;
                case 4 -> ;
                case 5 -> ;
                case 6 -> ;
                case 7 -> ;
                case 8 -> ;
                case 9 -> ;
                default -> MainAppUser.afficher(Textes.ERR_SAISI);
            }
        }
    }


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
        } while(choix == 0);

        return choix;
    }

    /* Affiche les menus de l'application */
    private static void afficher(String texte) {
        System.out.println(texte+"\n");

    }
}
