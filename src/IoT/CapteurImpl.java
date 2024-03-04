package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Un capteur permet de surveiller deux paramètres essentiels des cultures :
 *  - l’humidité du sol
 *  - température de l’air.
 *  Il effectue les relevés des deux paramètres précédents dans un intervalle régulier
 */
public class CapteurImpl extends UnicastRemoteObject implements Capteur { // Pas besoin d'implémenter Runnable car on implémente l'interface Capteur qui hérite de Runnable

    /** Un capteur est identifié par un code unique */
    private String identifiant;

    /** Un capteur possède des coordonnées GPS */
    private float[] gps;

    /** Un capteur possède un gestionnaire */
    private Gestionnaire leGestionnaire;

    /** Intervalle de temps (en seconde) entre deux relevés du capteur (5 secondes par défaut)*/
    private int intervalle;

    // TODO : rajouter dans le DiagDeClasse
    /** Indique si le thread est en cours d'enregistrement */
    private volatile boolean actif;

    /**
     * Par défaut :
     * - un capteur effectue un relevé toutes les 5 secondes
     * - un capteur est inactif actif = false
     * - un capteur n'a pas de gestionnaire
     * @throws RemoteException si erreur lors de la communication
     */
    public CapteurImpl(String identifiant, float latitude, float longitude) throws RemoteException {
        super();
        this.identifiant = identifiant;
        gps = new float[] {latitude,longitude};
        leGestionnaire = null;
        intervalle = 5;
        actif = false;
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
     * @return les coordonnées GPS sous forme de tableau de flottant (latitude, longitude).
     */
    @Override
    public float[] getGps() {
        return gps;
    }

    /**
     * Modifie l'intervalle entre deux relevés
     * @param intervalle entier représentant la valeur du nouvel intervalle de temps (en seconde) entre deux relevés
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void modifierIntervalle(int intervalle) throws RemoteException {
        this.intervalle = intervalle;
    }

    /**
     * Affecte un gestionnaire au Capteur
     * @param leGestionnaire gestionnaire a affecté au capteur
     */
    @Override
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
     * Retourne l'état de travail du capteur (actif ou inactif)
     */
    @Override
    public boolean enFonction() { // TODO : modifier DiagCLass
        return actif;
    }

    /**
     * Change l'état de travail du capteur (actif)
     */
    @Override
    public void onOff() { // TODO : modifier DiagCLass
        actif = !actif; // Si actif est true, cette instruction le rendra false, et vice-versa
    }

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     */
    @Override
    public void run() {
        this.onOff(); // Change l'état du capteur sur "actif" (this.actif = true)
        while(actif) {
            Releve unReleve = faireUnRelever();
            try {
                leGestionnaire.enregistrerValeur(this.identifiant, unReleve.temperature(),unReleve.tauxHumidite(), unReleve.Horodatage());
                System.out.println("Relevé de " + this.identifiant + " => " + unReleve.toString()+"\n");
                Thread.sleep(intervalle*1000); // 1000 ms = 1 sec.
            } catch (InterruptedException | SQLException | RemoteException e) { // Gestion de l'interruption du thread
                System.out.println("Erreur lors d'un relevé, celui-ci n'a pas été enregistré");
                this.onOff(); // Met à jour la variable actif si le thread est interrompu
            }
        }
    }
}
