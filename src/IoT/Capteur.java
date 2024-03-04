package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Capteur extends Remote, Runnable { // TODO : modifier DiagCLass

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
     * Affecte un gestionnaire au Capteur
     * @param leGestionnaire gestionnaire a affecté au capteur
     */
    public void setGestionnaire(Gestionnaire leGestionnaire); // TODO : modifier DiagCLass

    /**
     * Retourne l'état de travail du capteur (actif ou inactif) // TODO : modifier DiagCLass
     */
    public boolean enFonction();

    /**
     * Change l'état de travail du capteur (actif/inactif) // TODO : modifier DiagCLass
     * Principe de l'interrupteur ....
     */
    public void onOff();

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     */
    @Override
    public void run();

}
