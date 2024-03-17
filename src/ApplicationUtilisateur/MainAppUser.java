package ApplicationUtilisateur;

import Gestionnaire.Gestionnaire;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;


/**
 * Application de l'utilisateur.
 * Met à disposition les fonctionnalités décrites
 * dans le diagramme des cas d'utilisation.
 */
public abstract class MainAppUser {

    /** Le gestionnaire distant */
    protected static Gestionnaire leGestionnaire;

    /* -----------MAIN----------- */
    public static void main(String[] args) {

        /* Crée le registre RMI (s'il n'existe pas deja) */
        try {
            LocateRegistry.createRegistry(1099);
        } catch(java.rmi.server.ExportException e) {
            /* Un registre RMI existe deja sur le port 1099 ... on ne fait donc rien */
        } catch (RemoteException err) {
            System.out.println("Erreur lors de la création du registre");
        }

        try {
            GestionnaireNotificationImpl GestionnaireNotification  = new GestionnaireNotificationImpl();
            Naming.rebind("notification", GestionnaireNotification); // on enregistre le service de notification
            leGestionnaire = (Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire"); // on récupère le gestionnaire distant
            int choix, intervalle, duree;
            String idCapteur, idActionneur;

            Outils.afficher(Textes.ACCUEIL);
            while (true) {
                Outils.afficher(Textes.FCT);
                choix = Outils.demanderChoix();
                switch (choix) {
                    case 1 -> { // 1 - Ajouter un capteur
                        idCapteur = Outils.demanderCapteur();
                        Outils.afficher(leGestionnaire.ajouterCapteur(idCapteur));
                    }

                    case 2 -> { // 2 - Démarrer un capteur
                        idCapteur = Outils.demanderCapteur();
                        Outils.afficher(leGestionnaire.demarrerCapteur(idCapteur));
                    }

                    case 3 -> { // 3 - Stopper un capteur
                        idCapteur = Outils.demanderCapteur();
                        Outils.afficher(leGestionnaire.stopperCapteur(idCapteur));
                    }

                    case 4 ->  { // 4 - Retirer un capteur
                        idCapteur = Outils.demanderCapteur();
                        Outils.afficher(leGestionnaire.retirerCapteur(idCapteur));
                    }

                    case 5 -> Outils.afficher(leGestionnaire.listeCapteurs()); // 5 - Lister les capteurs

                    case 6 -> { // 6 - Voir le dernier relevé d'un capteur
                        idCapteur = Outils.demanderCapteur();
                        Outils.afficher("Voici le dernier relevé du capteur " + idCapteur + " : "
                                             + "\nTempérature | Taux d'humidité | Horodatage");
                        Outils.afficher(leGestionnaire.dernierReleve(idCapteur));
                    }

                    case 7 -> { // 7 - Obtenir des statistiques sur les relevés (moyenne et tendances)
                        duree = Outils.demanderDureeStat();
                        if (duree == 1) {
                            Outils.afficher(leGestionnaire.statsCapteursUneHeure());
                        } else { //  duree == 24
                            Outils.afficher(leGestionnaire.statsCapteursUneJournee());
                        }

                    }

                    case 8 -> { // 8 - Modifier l'intervalle de mesure pour un capteur
                        idCapteur = Outils.demanderCapteur();
                        intervalle = Outils.demanderIntervalle();
                        Outils.afficher(leGestionnaire.modifierIntervalle(intervalle, idCapteur));
                    }

                    case 9 -> { // 9 - Modifier l'intervalle de mesure pour tous les capteurs
                        intervalle = Outils.demanderIntervalle();
                        Outils.afficher(leGestionnaire.modifierIntervalleTous(intervalle));
                    }

                    case 10 -> { // 10 - Ajouter un actionneur
                        idActionneur = Outils.demanderActionneur();
                        Outils.afficher(leGestionnaire.ajouterActionneur(idActionneur));
                    }

                    case 11 -> { // 11 - Retirer un actionneur
                        idActionneur = Outils.demanderActionneur();
                        Outils.afficher(leGestionnaire.retirerActionneur(idActionneur));
                    }

                    case 12 -> { // 12 - Déclencher l'arrosage
                        idActionneur = Outils.demanderActionneur();
                        duree = Outils.demanderDureeArrosage();
                        Outils.afficher(leGestionnaire.demarrerArrosage(idActionneur, duree));
                    }

                    case 13 -> { // 13 - Obtenir l'état de l'arrosage
                        idActionneur = Outils.demanderActionneur();
                        Outils.afficher(leGestionnaire.etatArrosage(idActionneur));
                    }

                    case 14 -> Outils.afficher(leGestionnaire.listeActionneur()); // 14 - Lister les actionneurs

                    case 15 -> { // 15 - Quitter Agriconnect
                        Outils.afficher(Textes.ABIENTOT);
                        System.exit(0);
                    }

                    default -> Outils.afficher(Textes.ERR_SAISIE_FCT); // Erreur de saisie
                }
                Outils.afficher("Nombre de capteur actif : " + leGestionnaire.nbCapteurActif());
            }
        } catch (MalformedURLException | NotBoundException | SQLException | RemoteException exception) {
            Outils.afficher(Textes.EXCEPTION);
            System.exit(1);
        }
    }
}
