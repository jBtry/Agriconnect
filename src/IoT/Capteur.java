package IoT;

/**
 * Un capteur permet de surveiller deux paramètres essentiels des cultures :
 *  - l’humidité du sol
 *  - température de l’air.
 */
public class Capteur implements Runnable {

    /** Un capteur est identifié par un code unique */
    String identifiant;

    /** Un capteur possède des coordonnées GPS */
    float[] gps;

    public Capteur(String identifiant, float latitude, float longitude) {
        this.identifiant = identifiant;
        gps = new float[] {latitude,longitude};
    }


    /**
     * Périodiquement, le capteur remonte à la centrale les informations de température et d’humidité. Celle-ci les
     * enregistre, affiche sur la console les informations du capteur.
     */
    @Override
    public void run() {

    }


}
