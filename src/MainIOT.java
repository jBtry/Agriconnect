import Gestionnaire.Gestionnaire;
import IoT.Capteur;
import IoT.Outils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainIOT {

    /**
     * Crée 5 capteurs, affiche leurs informations
     * Récupère le gestionnaire (objet) distant et l'affecte aux 5 cpateurs
     * Lance un relevé toutes les 5 secondes sur les 5 capteurs
     * Au bout de 10 relevé on arrête.
     * On vide la table contenant la liste des capteurs sur le serveur
     * Le programme s'arrête
     * @param args non utilisé
     */
    public static void main(String args[]) {
        // TODO : Mieux gérer les exceptions dans les prochaines versions ...
        /* Contient les 5 capteurs */
        ArrayList<Capteur> capteurs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Capteur unCapteur = new Capteur("Capteur" + i, Outils.genererLatitudeAleatoires(), Outils.genererLongitudeAleatoires());
            capteurs.add(unCapteur);
        }
        /* Affichage des informations des 5 capteurs */
        System.out.println("Affichage des informations des 5 capteurs : \n");
        for (Capteur capteur : capteurs) {
            System.out.println(capteur.toString());
        }

        /* Récupération de l'objet gestionnaire distant */
        try {
            Gestionnaire leGestionnaire = (Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire");
            /* Enregistrement des capteurs */
            System.out.println("Enregistrement des capteurs ... \n");
            for (Capteur capteur : capteurs) {
                leGestionnaire.enregistrerCapteur(capteur.getIdentifiant(), capteur.getGps());
                capteur.setGestionnaire(leGestionnaire);
            }

            System.out.println("Démarrage des relevés ... \n");
            /* Démarrage des relevés */
            ArrayList<Thread> threads = new ArrayList<>(); // Contient les 5 threads, ils exécutent les relevés
            for (Capteur capteur : capteurs) {
                Thread thread = new Thread(capteur);
                threads.add(thread);
                thread.start();
            }

            /* Attente de la fin de l'exécution de chaque thread */
            for (Thread thread : threads) {
                thread.join();
            }
            System.out.println("Fin des relevés ... \n");

            String saisie;
            Scanner clavier = new Scanner(System.in);
            boolean saisieOk = false;
            do {
                System.out.println("Vous pouvez visualiser la base de données contenant les capteurs... \n"
                + "lorsque la touche 'x' sera pressé alors le processus de retrait des capteurs commencera ....");
                saisie = clavier.nextLine();
                if (saisie.matches("x")) {
                    saisieOk = true;
                }
            } while (!saisieOk);

            System.out.println("Retrait des capteurs ... \n");
            /* Retrait des capteurs */
            for (Capteur capteur : capteurs) {
                leGestionnaire.retirerCapteur(capteur.getIdentifiant());
            }
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

