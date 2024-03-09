package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Objects;

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
     * Par défaut :
     *  - un actionneur n'a pas de gestionnaire
     *  - un actionneur n'arrose pas, la durée de l'arrosage est donc à 0.
     * @throws RemoteException si erreur lors de la communication
     */
    protected ActionneurImpl(String identifiant, float latitude, float longitude) throws RemoteException {
        this.identifiant = identifiant;
        gps = new float[]{latitude, longitude};
        leGestionnaire = null;
        tempsRestantArrosage = 0;
        etatArrosage = false;
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
     * Permet de connaître l'état de l'arrosage actuel.
     *  True => en marche
     *  False => à l'arrêt
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @return l'état de l'arrosage actuel.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public boolean obtenirEtatArrosage(String idActionneur) throws RemoteException {
        return etatArrosage;
    }

    /**
     * Permet d'ordonner à un actionneur de déclencher l'arrosage.
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @param duree        entier représentant la durée d'arrosage en MINUTE
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void declencherArrosage(String idActionneur, int duree) throws RemoteException {
        // TODO : coder
    }

    /**
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @return le temps restant avant la fin de l'arrosage en MINUTE
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public int obtenirTempsRestantArrosage(String idActionneur) throws RemoteException {
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
                "\n-----------------------------------------------\n\n}";
    }
}
