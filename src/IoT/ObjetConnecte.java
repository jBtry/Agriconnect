package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface parente de Capteur et Actionneur
 */
public interface ObjetConnecte extends Remote {

    /**
     * @return les coordonnées GPS de l'objet connecté (Capteur ou Actionneur) sous forme de tableau de flottant (latitude, longitude).
     * @throws RemoteException
     */
    public float[] getGps() throws RemoteException;

    /**
     * Retourne l'état de travail de l'objet connecté (actif ou inactif)
     * @throws RemoteException
     */
    public boolean enFonction() throws RemoteException;

    /**
     * Change l'état de travail de l'objet connecté (actif/inactif)
     * Principe de l'interrupteur ....
     */
    public void onOff() throws RemoteException;

    /**
     * Affecte un gestionnaire à l'objet connecté
     * @param leGestionnaire gestionnaire a affecté à l'objet connecté
     */
    public void setGestionnaire(Gestionnaire leGestionnaire) throws RemoteException;

}
