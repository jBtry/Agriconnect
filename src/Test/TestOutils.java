package Test;
import IoT.Outils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Contient les tests unitaires sur les méthodes de la classe Outils.
 */
public abstract class TestOutils {

    private static int compteurReussite = 0;
    private static float valeurIncorrecte = 0;
    private final static int NOMBRE_ESSAI = 100;
    private static String raison = "";
    private static String dernierHorodatage = "";

   /* Test de la méthode genererLatitudeAleatoires()
    * On vérifie la méthode en générant 100 valeurs aléatoirement,
    * si l'une d'elle n'est pas comprise entre -90 et 90 le test échoue.
    */
    public static void testGenererLatitudeAleatoires() {
        for(int i = 0; i < NOMBRE_ESSAI; i++) {
            float latitude = Outils.genererLatitudeAleatoires();
            if (-90 < latitude && latitude < 90) {
                compteurReussite++;
            } else {
                valeurIncorrecte = latitude;
                break;
            }
        }
        if (compteurReussite == NOMBRE_ESSAI) {
            System.out.println("Réussite du test : toutes les latitudes générées sont valides.");
            compteurReussite = 0;
        } else {
            System.out.println("Echec du test : une des latitudes générées est valide."
                                + "Valeur incorrecte => : " + valeurIncorrecte);
        }
    }

    /* Test de la méthode genererLongitudeAleatoires()
     * On vérifie la méthode en générant 100 valeurs aléatoirement,
     * si l'une d'elle n'est pas comprise entre -180 et 180 le test échoue.
     */
    public static void testGenererLongitudeAleatoires() {
        for(int i = 0; i < NOMBRE_ESSAI; i++) {
            float longitude = Outils.genererLongitudeAleatoires();
            if (-180 < longitude && longitude < 180) {
                compteurReussite++;
            } else {
                valeurIncorrecte = longitude;
                break;
            }
        }
        if (compteurReussite == NOMBRE_ESSAI) {
            System.out.println("Réussite du test : toutes les longitudes générées sont valides.");
            compteurReussite = 0;
        } else {
            System.out.println("Echec du test : une des longitudes générées est hors de l'intervalle valide."
                    + "Valeur incorrecte => : " + valeurIncorrecte);
        }
    }

    /* Test de la méthode genererTemperatureAleatoire()
     * On vérifie la méthode en générant 100 valeurs aléatoirement,
     * si l'une d'elle n'est pas comprise entre -10 et 50 le test échoue.
     */
    public static void testGenererTemperatureAleatoire() {
        for(int i = 0; i < NOMBRE_ESSAI; i++) {
            float temperature = Outils.genererTemperatureAleatoire();
            if (-10 <= temperature && temperature <= 50) {
                compteurReussite++;
            } else {
                valeurIncorrecte = temperature;
                break;
            }
        }
        if (compteurReussite == NOMBRE_ESSAI) {
            System.out.println("Réussite du test : toutes les températures générées sont dans l'intervalle valide.");
            compteurReussite = 0;
        } else {
            System.out.println("Echec du test : une température générée est hors de l'intervalle valide."
                    + " Valeur incorrecte => : " + valeurIncorrecte);
        }
    }

    /* Test de la méthode genererTauxHumiditeAleatoire()
     * On vérifie la méthode en générant 100 valeurs aléatoirement,
     * si l'une d'elle n'est pas comprise entre 0 et 100 le test échoue.
     */
    public static void testGenererTauxHumiditeAleatoire() {
        for(int i = 0; i < NOMBRE_ESSAI; i++) {
            float tauxHumidite = Outils.genererTauxHumiditeAleatoire();
            if (0 <= tauxHumidite && tauxHumidite <= 100) {
                compteurReussite++;
            } else {
                valeurIncorrecte = tauxHumidite;
                break;
            }
        }
        if (compteurReussite == NOMBRE_ESSAI) {
            System.out.println("Réussite du test : tous les taux d'humidité générés sont dans l'intervalle valide.");
            compteurReussite = 0;
        } else {
            System.out.println("Echec du test : un taux d'humidité généré est hors de l'intervalle valide."
                    + " Valeur incorrecte => : " + valeurIncorrecte);
        }
    }

    /* Test de la méthode horodatage()
     * On vérifie la méthode en générant une valeur,
     * si elle n'a pas le bon format le test échoue.
     */
    public static void testHorodatage() {

        String horodatage = Outils.horodatage();
        try {
            LocalDateTime.parse(horodatage, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("Réussite du test : l'horodatage a bien été généré. "
                    + "Vérifez si la valeur est correcte");
        } catch (DateTimeParseException e) {
            raison = "Le format de l'horodatage n'est pas correct.";
            System.out.println("Echec du test : il y a une erreur dans la génération des horadatages."
                    + "Raison => : " + raison);
        }
    }



    /* Lancement des tests */
    public static void main(String args[]) {
        System.out.println("Test de la méthode genererLatitudeAleatoires()");
        testGenererLatitudeAleatoires();
        System.out.println("-------------------------------------------------");
        System.out.println("Test de la méthode genererLongitudeAleatoires()");
        testGenererLongitudeAleatoires();
        System.out.println("-------------------------------------------------");
        System.out.println("Test de la méthode genererTemperatureAleatoire()");
        testGenererTemperatureAleatoire();
        System.out.println("-------------------------------------------------");
        System.out.println("Test de la méthode genererTauxHumiditeAleatoire()");
        testGenererTauxHumiditeAleatoire();
        System.out.println("-------------------------------------------------");
        System.out.println("Test de la méthode horodatage()");
        testHorodatage();
        System.out.println("-------------------------------------------------");
    }
}
