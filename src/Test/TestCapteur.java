package Test;

import IoT.Capteur;
import IoT.Releve;

/**
 * Classe de test pour la classe Capteur de l'application IoT.
 */
public class TestCapteur {

    /* Test des méthodes de la classe Capteur. */
    public static void testConstructeurEtGetters() {
        System.out.println("Test du constructeur et des getters...");
        Capteur capteur = new Capteur("C123", 45.77689f, 4.85764f);
        int nbrTestValide = 0;
        final int NBR_TEST_TOTAL = 3;

        if (capteur.getIdentifiant().equals("C123")) {
            nbrTestValide++;
        } else {
            System.out.println("Échec à l'étape de vérification de l'identifiant.");
        }

        if (capteur.getGps()[0] == 45.77689f && capteur.getGps()[1] == 4.85764f) {
            nbrTestValide++;
        } else {
            System.out.println("Échec à l'étape de vérification des coordonnées GPS.");
        }

        if (capteur.toString().contains("identifiant='C123'")) {
            nbrTestValide++;
        } else {
            System.out.println("Échec à l'étape de vérification de la méthode toString.");
        }

        if (nbrTestValide == NBR_TEST_TOTAL) {
            System.out.println("Tous les tests du constructeur et des getters ont réussi.");
        } else {
            System.out.println("Échec, " + nbrTestValide + " sur " + NBR_TEST_TOTAL + " tests ont réussi.");
        }
    }

    /* Test de la méthode faireUnRelever() de la classe Capteur.
     * Comme les valeurs sont aléatoires, nous ne pouvons pas vérifier leurs exactitudes.
     * Nous pouvons seulement vérifier que le relevé n'est pas null.
     * Les valeurs générées sont deja testées dans TestOutils.
     */
    public static void testFaireUnRelever() {
        System.out.println("Test de la méthode faireUnRelever...");
        Capteur capteur = new Capteur("C123", 45.77689f, 4.85764f);
        Releve releve = capteur.faireUnRelever();

        if (releve != null) {
            System.out.println("Le test de faireUnRelever a réussi.");
        } else {
            System.out.println("Échec du test de faireUnRelever.");
        }
    }

    /* Lancement des tests. */
    public static void main(String[] args) {
        System.out.println("Début des tests pour la classe Capteur.");
        testConstructeurEtGetters();
        testFaireUnRelever();
        System.out.println("Fin des tests pour la classe Capteur.");
    }
}
