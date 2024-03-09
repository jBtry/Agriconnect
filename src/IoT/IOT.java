package IoT;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface parente de Capteur et Actionneur
 * Hérite de l'interface IOT qui, elle-même, hérite de l'interface java.rmi.remote
 */
public interface IOT extends Remote {

    /**
     * @return les coordonnées GPS de l'IOT (Capteur ou Actionneur) sous forme de tableau de flottant (latitude, longitude).
     * @throws RemoteException si erreur lors de la communication.
     */
    public float[] getGps() throws RemoteException;

}
