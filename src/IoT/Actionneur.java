package IoT;

import java.rmi.RemoteException;

/**
 * Méthode accessible à distance pour un Actionneur
 * Hérite de l'interface IOT qui, elle-même, hérite de l'interface java.rmi.remote
 */
public interface Actionneur extends IOT {

    /**
     * Permet de connaître l'état de l'arrosage actuel.
     *  True => en marche
     *  False => à l'arrêt
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @return l'état de l'arrosage actuel.
     * @throws RemoteException si erreur lors de la communication.
     */
    public boolean obtenirEtatArrosage(String idActionneur) throws RemoteException;

    /**
     * Permet d'ordonner à un actionneur de déclencher l'arrosage.
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @param duree entier représentant la durée d'arrosage en MINUTE
     * @throws RemoteException si erreur lors de la communication.
     */
    public void declencherArrosage(String idActionneur, int duree) throws RemoteException;

    /**
     * @param idActionneur chaine de caractères représentant l'identifiant de l'actionneur à consulter.
     * @return le temps restant avant la fin de l'arrosage en MINUTE
     * @throws RemoteException si erreur lors de la communication.
     */
    public int obtenirTempsRestantArrosage(String idActionneur) throws RemoteException;


}
