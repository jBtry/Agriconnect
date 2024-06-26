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
public class CapteurImpl extends UnicastRemoteObject implements Capteur {

    /**
     * Un capteur est identifié par un code unique
     */
    private final String identifiant;

    /**
     * Un capteur possède des coordonnées GPS
     * gps[O] → latitude, gps[1] → longitude
     */
    private final float[] gps;

    /**
     * Un capteur possède un gestionnaire
     */
    private Gestionnaire leGestionnaire;

    /**
     * Intervalle de temps (en seconde) entre deux relevés du capteur (5 secondes par défaut)
     */
    private int intervalle;

    /**
     * Indique si le thread est en cours d'enregistrement
     */
    private volatile boolean actif; // volatile pour être accessible par différents threads

    /**
     * Taux d'humidité fixe relevé par un capteur
     * Sert uniquement à montrer l'impact du système d'arrosage...
     * Par défaut, c'est-à-dire avant arrosage le taux d'humidité lors d'un relevé est 50%.
     * Il s'incrémente par la suite en fonction des différents cycles d'arrosage successif
     */
    private float simulationTauxHumidite;

    /**
     * Par défaut :
     * - un capteur effectue un relevé toutes les 5 secondes
     * - un capteur est inactif (actif = false)
     * - un capteur n'a pas de gestionnaire
     * @throws RemoteException 
     */
    public CapteurImpl(String identifiant, float latitude, float longitude) throws RemoteException {
        super();
        this.identifiant = identifiant;
        gps = new float[]{latitude, longitude};
        leGestionnaire = null;
        intervalle = 5;
        actif = false;
        simulationTauxHumidite = 50;
    }

    /**
     * @return les informations du capteur
     */
    @Override
    public String toString() {
        String infoGestionnaire = Objects.isNull(this.leGestionnaire) ? "Pas de Gestionnaire" : leGestionnaire.toString();
        return "identifiant='" + identifiant + '\'' +
                ", gps=" + Arrays.toString(gps) +
                ", leGestionnaire=" + infoGestionnaire +
                "\n-----------------------------------------------\n\n}";
    }


    /**
     * Permet d'obtenir les coordonnées GPS du capteur
     * @return les coordonnées GPS sous forme de tableau de flottant (latitude, longitude).
     * @throws RemoteException
     */
    @Override
    public float[] getGps() throws RemoteException {
        return gps;
    }

    /**
     * Modifie l'intervalle entre deux relevés
     *
     * @param intervalle entier représentant la valeur du nouvel intervalle de temps (en seconde) entre deux relevés
     * @throws RemoteException .
     */
    @Override
    public void modifierIntervalle(int intervalle) throws RemoteException {
        this.intervalle = intervalle;
    }

    /**
     * Affecte un gestionnaire au Capteur
     * @param leGestionnaire gestionnaire a affecté au capteur
     * @throws RemoteException
     */
    @Override
    public void setGestionnaire(Gestionnaire leGestionnaire) throws RemoteException {
        this.leGestionnaire = leGestionnaire;
    }

    /**
     * Le capteur relève la température ambiante et le taux d'humidité à instant T.
     */
    private Releve faireUnReleve() {
        /* Génération de la température */
        float temp = Outils.genererTemperatureAleatoire();
        /* Génération d'un taux d'humidité */
        float tauxH = simulationTauxHumidite; // Outils.genererTauxHumiditeAleatoire(); => pas utilisé dans la V3
        /* Génération de l'horodatage */
        String horodatage = Outils.horodatage();
        return new Releve(temp, tauxH, horodatage);
    }

    /**
     * Retourne l'état de travail du capteur (actif ou inactif)
     * @throws RemoteException
     */
    @Override
    public boolean enFonction() throws RemoteException {
        return actif;
    }

    /**
     * Change l'état de travail du capteur (variable actif)
     * @throws RemoteException
     */
    @Override
    public void onOff() throws RemoteException {
        actif = !actif; // Si actif est true, cette instruction le rendra false, et vice-versa
    }

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     * @throws RemoteException
     */
    @Override
    public void demarrerEnregistrementReleve() throws RemoteException {
        actif = !actif; // Change l'état du capteur sur "actif" (this.actif = true)

       /* ----------- Explication sur l'utilisation d'une classe interne anonyme ----------
        * La méthode run doit être accessible à distance pour que le Gestionnaire puisse justement, démarrer, à distance, sur le capteur, l'enregistrement des relevés
        * L'interface Capteur ne peut pas implémenter à la fois java.rmi.Remote et java.lang.Runnable
        * En effet, en RMI, toutes les méthodes d'une interface distante doivent déclarer la possible propagation de l'exception java.rmi.RemoteException.
        * Cependant, la méthode run() de l'interface Runnable ne déclare aucune exception, et certainement donc pas RemoteException.
        * On ne peut ainsi pas directement créer un objet distant RMI à partir d'une interface capteur qui implémenterait Runnable
        * dans le cas où cela implique d'inclure run() comme une méthode distante...
        * ----------------------------------------
        */
        Thread releve = new Thread(new Runnable() { // Classe interne Anonyme implémentant Runnable, on pourrait utiliser une expression lambda pour simplifier la syntaxe ...
            @Override
            public void run() {
                while (actif) {
                    Releve unReleve = faireUnReleve();
                    try {
                        leGestionnaire.enregistrerValeur(identifiant, unReleve.temperature(), unReleve.tauxHumidite(), unReleve.Horodatage());
                        System.out.println("Relevé de " + identifiant + " => " + unReleve + "\n");
                        Thread.sleep(intervalle * 1000); // 1000 ms = 1 sec.
                    } catch (InterruptedException | SQLException | RemoteException e) { // Gestion de l'interruption du thread
                        System.out.println("Erreur lors d'un relevé, celui-ci n'a pas été enregistré");
                        actif = !actif; // Met à jour la variable actif si le thread est interrompu (this.actif = false)
                    }
                }
            }
        });
        releve.start();
    }

    /**
     * Méthode permettant d'influer sur le taux humidité
     * Utile uniquement pour simuler l'impact de l'arrosage
     * @throws RemoteException
     */
    @Override
    public void influerTauxHumidite() throws RemoteException {
        simulationTauxHumidite++;
    }
}
