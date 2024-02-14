package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Un capteur permet de surveiller deux paramètres essentiels des cultures :
 *  - l’humidité du sol
 *  - température de l’air.
 */
public class Capteur implements Runnable {

    /** Un capteur est identifié par un code unique */
    private String identifiant;

    /** Un capteur possède des coordonnées GPS */
    private float[] gps;

    /** Un capteur possède un gestionnaire */
    private Gestionnaire leGestionnaire;

    public Capteur(String identifiant, float latitude, float longitude) {
        this.identifiant = identifiant;
        gps = new float[] {latitude,longitude};
        leGestionnaire = null; // Un capteur peut ne pas avoir de gestionnaire.
    }

    /**
     * @return les informations du capteur
     */
    @Override
    public String toString() {
        String infoGestionnaire = Objects.isNull(this.leGestionnaire) ? "Pas de Gestionnaire" : leGestionnaire.toString() ;
        return  "identifiant='" + identifiant + '\'' +
                ", gps=" + Arrays.toString(gps) +
                ", leGestionnaire=" + infoGestionnaire +
                "\n-----------------------------------------------\n\n}";
    }

    /**
     * @return l'identifiant du capteur
     */
    public String getIdentifiant() {
        return identifiant;
    }


    /**
     * @return les coordonnées GPS sous forme de tableau de flottant (latitude, longitude).
     */
    public float[] getGps() {
        return gps;
    }

    /**
     * Affecte un gestionnaire au Capteur
     * @param leGestionnaire gestionnaire a affecté au capteur
     */
    public void setGestionnaire(Gestionnaire leGestionnaire) {
        this.leGestionnaire = leGestionnaire;
    }

    /**
     * Le capteur relève la température ambiante et le taux d'humidité à instant T.
     */
    public Releve faireUnRelever() {

        /* Génération de la température */
        float temp = Outils.genererTemperatureAleatoire();
        /* Génération d'un taux d'humidité */
        float tauxH = Outils.genererTauxHumiditeAleatoire();
        /* Génération de l'horodatage */
        String horodatage = Outils.horodatage();
        return new Releve(temp,tauxH,horodatage);
    }

    /**
     * Simule dix relevés
     * (intervalle de 5 secondes)
     */
    @Override
    public void run() {
        for (int i = 0 ; i < 10; i++) {
            Releve unReleve = faireUnRelever();
            try {
                leGestionnaire.enregistrerValeur(this.identifiant, unReleve.temperature(),unReleve.tauxHumidite(), unReleve.Horodatage());
                System.out.println("Relevé de " + this.identifiant + " => " + unReleve.toString()+"\n");
                Thread.sleep(5000); // 5000 ms = 5 sec.
            } catch (InterruptedException | SQLException | RemoteException e) {
                System.out.println("Erreur lors d'un relevé, celui-ci n'a pas été enregistré");
            }
        }
    }
}
