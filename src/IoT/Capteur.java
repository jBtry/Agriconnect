package IoT;

import java.rmi.RemoteException;

public interface Capteur extends java.rmi.Remote {

    /**
     * @return les coordonnées GPS sous forme de tableau de flottant (latitude, longitude).
     * @throws RemoteException si erreur lors de la communication.
     */
    public float[] getGps() throws RemoteException;

    /**
     * Modifie l'intervalle entre deux relevés
     * @param intervalle entier représentant la valeur de l'intervalle entre deux relevés (en seconde)
     * @throws RemoteException si erreur lors de la communication.
     */
    public void modifierIntervalle(int intervalle) throws RemoteException;

    /**
     * @return les informations du capteur
     */
    @Override
    public String toString() throws RemoteException;
}
