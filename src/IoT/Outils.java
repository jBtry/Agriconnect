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

    /** Température précédente générée aléatoirement */
    private static float tempPrecedente = 0;

    /** Taux d'humidité précédent généré aléatoirement */
    private static float tauxHPrecedent = 0;

    /** Génère une latitude aléatoirement
     * @return un flottant représentant une latitude
     */
    protected static float genererLatitudeAleatoires() {
        float latitude = -90 + (90 - (-90)) * ALEATOIRE.nextFloat();
        return latitude;
    }

    /** Génère une longitude aléatoirement
     * @return flottant représentant une longitude
     */
    protected static float genererLongitudeAleatoires() {
        float longitude = (-180 + (180 - (-180)) * ALEATOIRE.nextFloat());
        return longitude;
    }


    /**
     * @return une température en °C aléatoire
     */
    protected static float genererTemperatureAleatoire() {
        float resultat, ajustement;
        int minTemp = -10; // Température minimale
        int maxTemp = 40;  // Température maximale
        if (tempPrecedente == 0) { // On génère une première température aléatoirement ...
            resultat = minTemp + (maxTemp - minTemp) * ALEATOIRE.nextFloat();
            resultat = Math.round(resultat * 10)/10.0f; // Arrondit le résultat à 1 chiffre après la virgule
            tempPrecedente = resultat;
            return resultat; // Arrondit le résultat à 1 chiffre après la virgule
        } else { // ... pour que celles d'après suivent et ainsi garder une cohérence pour la fonctionnalité "statistique"
            ajustement = (4 * ALEATOIRE.nextFloat() - 2); // Donne un nombre entre -2 et 2, c'est notre facteur d'ajustement aléatoire
            ajustement = Math.round(ajustement * 10)/10.0f; // Arrondit le résultat à 1 chiffre après la virgule
            resultat = tempPrecedente + ajustement; // Nouvelle température générée aléatoirement dans la continuité de l'ancienne.
            tempPrecedente = resultat; // Mise à jour de la dernière température générée
            return resultat;
        }
    }


    /**
     * @return un taux d'humidité aléatoire
     */
    protected static float genererTauxHumiditeAleatoire() {
        float resultat, ajustement;;
        int minHumidite = 40;   // Humidité minimale en pourcentage
        int maxHumidite = 80; // Humidité maximale en pourcentage
        if (tauxHPrecedent == 0) { // On génère un premier taux d'humidité aléatoirement ...
            resultat = minHumidite + (maxHumidite - minHumidite) * ALEATOIRE.nextFloat();
            tauxHPrecedent = resultat;
            return Math.round(resultat * 10)/10.0f; // Arrondit le résultat à 1 chiffre après la virgule
        } else { // ... pour que ceux d'après suivent et ainsi garder une cohérence pour la fonctionnalité "statistique"
            ajustement = (4 * ALEATOIRE.nextFloat() - 2); // Donne un nombre entre -2 et 2, c'est notre facteur d'ajustement aléatoire
            resultat = tauxHPrecedent + ajustement; // Nouveaux taux d'humidité généré aléatoirement dans la continuité de l'ancien.
            tauxHPrecedent = resultat; // Mise à jour du dernier taux d'humidité généré
            return Math.round(resultat * 10)/10.0f; // Arrondit le résultat à 1 chiffre après la virgule
        }
    }


    /**
     * @return une chaine de caractère l'horodatage au moment de l'éxecution de la méthode au format : "yyyy-MM-dd HH:mm:ss".
     */
    protected static String horodatage() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // On a défini le format de l'horodatage
        String horodatage = LocalDateTime.now().format(format); // On récupère l'horodatage, on lui applique le format
        return  horodatage;
    }

}
