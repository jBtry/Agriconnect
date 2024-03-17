package ApplicationUtilisateur;

import Gestionnaire.Gestionnaire;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;


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
 */
public abstract class MainAppUser {

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
                MainAppUser.afficher(Textes.ERR_SAISIE_FCT);
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

    /* Demande de saisir l'identifiant d'un actionneur */
    private static String demanderActionneur() {
        Scanner clavier = new Scanner(System.in);
        MainAppUser.afficher(Textes.ACTIONNEUR);
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
                MainAppUser.afficher(Textes.ERR_SAISIE_INTERVALLE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix == 0);
        return choix;
    }

    /* Demande de saisir la durée sur laquelle obtenir les statistiques */
    private static int demanderDureeStat() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            MainAppUser.afficher(Textes.DUREE_STATS);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISIE_DUREE_STATS);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix < 1 | choix > 2);
        return choix == 1 ? 24 : 1; // 24h ou 1h ?
    }

    /* Demande de saisir la durée de l'arrosage */
    private static int demanderDureeArrosage() {
        Scanner clavier = new Scanner(System.in);
        int choix;
        do {
            MainAppUser.afficher(Textes.DUREE_ARROSAGE);
            try {
                choix = clavier.nextInt();
            } catch (InputMismatchException err) {
                MainAppUser.afficher(Textes.ERR_SAISIE_DUREE_ARROSAGE);
                clavier.next(); // Important pour nettoyer le buffer et éviter une boucle infinie
                choix = 0;
            }
        } while (choix == 0);
        return choix;
    }



    /* Affiche les menus de l'application */
    protected static void afficher(String texte) {
        System.out.println(texte + "\n");

    }

    // TODO : expliquer que l'on crée les gestionnaires dans le cas ou on est pas sur le même machine ....
    /* -----------MAIN----------- */
    public static void main(String[] args) {

        /* Crée le registre RMI (s'il n'existe pas deja) */
        try {
            LocateRegistry.createRegistry(1099);
        } catch(java.rmi.server.ExportException e) {
            // Un registre RMI existe deja sur le port 1099 ... on ne fait donc rien
        } catch (RemoteException err) {
            System.out.println("Erreur lors de la création du registre");
        }

        try {
            GestionnaireNotificationImpl GestionnaireNotification  = new GestionnaireNotificationImpl();
            Naming.rebind("notification", GestionnaireNotification);
            leGestionnaire = (Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire");
            int choix, intervalle, duree;
            String idCapteur, idActionneur;
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
                        MainAppUser.afficher("Voici le dernier relevé du capteur " + idCapteur + " : "
                                             + "\nTempérature | Taux d'humidité | Horodatage");
                        MainAppUser.afficher(leGestionnaire.dernierReleve(idCapteur));
                    }

                    case 7 -> { // 7 - Obtenir des statistiques sur les relevés (moyenne et tendances)
                        duree = MainAppUser.demanderDureeStat();
                        if (duree == 1) {
                            MainAppUser.afficher(leGestionnaire.statsCapteursUneHeure());
                        } else { //  duree == 24
                            MainAppUser.afficher(leGestionnaire.statsCapteursUneJournee());
                        }

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

                    case 10 -> { // 10 - Ajouter un actionneur
                        idActionneur = MainAppUser.demanderActionneur();
                        MainAppUser.afficher(leGestionnaire.ajouterActionneur(idActionneur));
                    }

                    case 11 -> { // 11 - Retirer un actionneur
                        idActionneur = MainAppUser.demanderActionneur();
                        MainAppUser.afficher(leGestionnaire.retirerActionneur(idActionneur));
                    }

                    case 12 -> { // 12 - Déclencher l'arrosage
                        idActionneur = MainAppUser.demanderActionneur();
                        duree = MainAppUser.demanderDureeArrosage();
                        MainAppUser.afficher(leGestionnaire.demarrerArrosage(idActionneur, duree));
                    }

                    case 13 -> { // 13 - Obtenir l'état de l'arrosage
                        idActionneur = MainAppUser.demanderActionneur();
                        MainAppUser.afficher(leGestionnaire.etatArrosage(idActionneur));
                    }

                    case 14 -> MainAppUser.afficher(leGestionnaire.listeActionneur()); // 14 - Lister les actionneurs

                    case 15 -> { // 15 - Quitter Agriconnect
                        MainAppUser.afficher(Textes.ABIENTOT);
                        System.exit(0);
                    }

                    default -> MainAppUser.afficher(Textes.ERR_SAISIE_FCT); // Erreur de saisie
                }
                MainAppUser.afficher("Nombre de capteur actif : " + leGestionnaire.nbCapteurActif());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch(RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
