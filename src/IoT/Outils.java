package IoT;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Met à disposition des méthodes permettant de générer des informations telles que :
 * - Une latitude
 * - Une longitude
 * - Une température
 * - Un taux d'humidité
 * - Un horodatage
 */
public abstract class Outils {

    private static final Random ALEATOIRE = new Random();

    /** Génère une latitude aléatoirement
     * @return un flottant représentant une latitude
     */
    public static float genererLatitudeAleatoires() {
        float latitude = -90 + (90 - (-90)) * ALEATOIRE.nextFloat();
        return latitude;
    }

    /** Génère une longitude aléatoirement
     * @return flottant représentant une longitude
     */
    public static float genererLongitudeAleatoires() {
        float longitude = (-180 + (180 - (-180)) * ALEATOIRE.nextFloat());
        return longitude;
    }


    /**
     * @return une température en °C aléatoire
     */
    public static float genererTemperatureAleatoire() { //TODO : arrondir le resultat à 1 chiffre après la virgule
        float minTemp = -10; // Température minimale
        float maxTemp = 50;  // Température maximale
        return minTemp + (maxTemp - minTemp) * ALEATOIRE.nextFloat();
    }


    /**
     * @return un taux d'humidité aléatoire
     */
    public static float genererTauxHumiditeAleatoire() { //TODO : arrondir le resultat à 1 chiffre après la virgule
        int minHumidite = 0;   // Humidité minimale en pourcentage
        int maxHumidite = 100; // Humidité maximale en pourcentage
        return minHumidite + (maxHumidite - minHumidite) * ALEATOIRE.nextFloat();
    }


    /**
     * @return une chaine de caractère l'horodatage au moment de l'éxecution de la méthode au format : "yyyy-MM-dd HH:mm:ss".
     */
    public static String horodatage() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // On a défini le format de l'horodatage
        String horodatage = LocalDateTime.now().format(format); // On récupère l'horodatage, on lui applique le format
        return  horodatage;
    }

}
