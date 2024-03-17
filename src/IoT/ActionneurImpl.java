package IoT;

import Gestionnaire.Gestionnaire;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;

/**
 * Un actionneur permet de démarrer l'arrosage des cultures pour un certain temps
 */
public class ActionneurImpl extends UnicastRemoteObject implements Actionneur  {

    /**
     * Un actionneur est identifié par un code unique
     */
    private String identifiant;

    /**
     * Un actionneur possède des coordonnées GPS
     * gps[O] => latitude, gps[1] => longitude
     */
    private float[] gps;

    /**
     * Un actionneur possède un gestionnaire
     */
    private Gestionnaire leGestionnaire;

    /**
     * Un actionneur possède un état d'arrosage
     * True => en marche
     * False => à l'arrêt
     */
    private boolean etatArrosage;

    /**
     * Contient le temps restant avant arrêt de l'arrosage
     * (en SECONDE)
     */
    private int tempsRestantArrosage;

    /**
     * Un actionneur, lorsqu'il active l'arrosage, influe sur
     * le taux d'humidité d'une zone.
     */
    private String zone;

    /** // TODO : rajouter @param ...
     * Par défaut :
     *  - un actionneur n'a pas de gestionnaire
     *  - un actionneur n'arrose pas, la durée de l'arrosage est donc à 0.
     * @throws RemoteException si erreur lors de la communication
     */
    protected ActionneurImpl(String identifiant, float latitude, float longitude, String Zone) throws RemoteException {
        this.identifiant = identifiant;
        gps = new float[]{latitude, longitude};
        leGestionnaire = null;
        tempsRestantArrosage = 0;
        etatArrosage = false;
        this.zone = Zone;
    }

    /**
     * @return les coordonnées GPS sous forme de tableau de flottant (latitude, longitude).
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public float[] getGps() throws RemoteException {
        return gps;
    }


    /**
     * Permet d'ordonner à un actionneur de déclencher l'arrosage sur une durée déterminée.
     * Déclenche un minuteur, à la fin de celui-ci l'arrosage s'arrête.
     * @param duree entier représentant la durée d'arrosage en MINUTE
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void declencherArrosage(int duree) throws RemoteException, MalformedURLException, NotBoundException {
        onOff(); // arrosage en marche
        Timer minuteur = new Timer(); // création d'un minuteur
        tempsRestantArrosage = duree * 60; // conversion minute => seconde
        /* Dans le sujet, on fait l'hypothèse que l'actionneur connait les capteurs dans sa zone,
         * cette hypothèse sert juste à prouver que la méthode déclencherArrosage() fonctionne et sert aussi à modéliser son impact.
         * Dans un déploiement réel, l'actionneur n'a pas besoin de connaître les capteurs de sa zone car le fait d'arroser fait naturellement augmenter l'humidité de la zone ...
         * De plus, comme cette partie de code n'existera pas dans l'application qui sera déployé dans la vraie vie,
         * on ne passe donc pas par le Gestionnaire pour utiliser la méthode qui influe sur les relevés des capteurs, on simplifie en simulant une liaison direct Actionneur <=> Capteur ...
         */ // TODO : mettre dans le rapport
        String[] nomsCapteursZone =  zone.equals("Zone 1") ? new String[]{"C1", "C2", "C3"} : new String[]{"C4", "C5", "C6"}; // voir classe abstraite GPS pour plus de détails ...
        ArrayList<Capteur> listeCapteursZone = new ArrayList<>();
        for (int i=0; i < nomsCapteursZone.length; i++) {
            Capteur unCapteur = (Capteur) Naming.lookup(nomsCapteursZone[i]);
            listeCapteursZone.add(unCapteur);
        }
        minuteur.scheduleAtFixedRate(new TimerTask() {
            int nbSeconde = 0; // Toute les 10 secondes, +1% d'humidité sur les relevés des capteurs
            @Override
            public void run() { // méthode executé toutes les secondes par le minuteur
                if (tempsRestantArrosage > 0) { // l'arrosage n'est pas encore terminé
                    tempsRestantArrosage--; // le temps s'écoule ...
                    nbSeconde++; // pour influencer toutes les 10 secondes les relevé des capteurs
                    if(nbSeconde == 10) { // +1% d'humidité toute les 10 secondes
                        for(Capteur capteur : listeCapteursZone) {
                            try {
                                capteur.influerTauxHumidite(); // on influence les relevés des capteurs
                            } catch (RemoteException e) {
                                ; /* On ne fait rien, la communication a échoué, mais l'application doit continuer à fonctionner
                                   * Très peu probable que ce cas arrive => localhost.
                                   */
                            }
                        }
                        nbSeconde = 0; // on remet le compteur de seconde à 0...
                    }// else, on ne fait rien
                } else { // fin du minuteur, l'arrosage est terminé
                    minuteur.cancel(); // Arrête le minuteur
                    try {
                        onOff(); // arrêt de l'arrosage
                        leGestionnaire.notificationFinArrosage(identifiant, duree);
                    } catch (RemoteException e) { // TODO Expliquer ...
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000); // 1000 ms = 1 sec
        /*
         * 0 représente le délai, en millisecondes, avant de démarrer le minuteur.
         * 1000 représente le délai, en millisecondes, entre deux taches (méthode run()) successives
         */
    }

    /**
     * @return le temps restant avant la fin de l'arrosage en SECONDE
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public int obtenirTempsRestantArrosage() throws RemoteException {
        return tempsRestantArrosage;
    }

    /**
     * @return les informations de l'actionneur
     */
    @Override
    public String toString() {
        String infoGestionnaire = Objects.isNull(this.leGestionnaire) ? "Pas de Gestionnaire" : leGestionnaire.toString();
        return "identifiant='" + identifiant + '\'' +
                ", gps=" + Arrays.toString(gps) +
                ", leGestionnaire=" + infoGestionnaire +
                ", zone=" + zone +
                "\n-----------------------------------------------\n\n}";
    }

    /**
     * Retourne l'état de travail de l'actionneur (actif ou inactif)
     */
    @Override
    public boolean enFonction() throws RemoteException {
        return etatArrosage;
    }

    /**
     * Change l'état de travail de l'actionneur (variable etatArrosage)
     */
    @Override
    public void onOff() throws RemoteException {
        etatArrosage = !etatArrosage; // Si actif est true, cette instruction le rendra false, et vice-versa
    }

    /**
     * Affecte un gestionnaire à l'actionneur
     * @param leGestionnaire gestionnaire a affecté à l'actionneur
     */
    @Override
    public void setGestionnaire(Gestionnaire leGestionnaire) throws RemoteException {
        this.leGestionnaire = leGestionnaire;
    }
}
