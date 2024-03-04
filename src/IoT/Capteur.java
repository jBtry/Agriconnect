package IoT;

import Gestionnaire.Gestionnaire;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Capteur extends Remote { // TODO : modifier DiagCLass

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
    public void setGestionnaire(Gestionnaire leGestionnaire) throws RemoteException; // TODO : modifier DiagCLass

    /**
     * Retourne l'état de travail du capteur (actif ou inactif)
     */
    public boolean enFonction() throws RemoteException; // TODO : modifier DiagCLass

    /**
     * Change l'état de travail du capteur (actif/inactif)
     * Principe de l'interrupteur ....
     */
    public void onOff() throws RemoteException; // TODO : modifier DiagCLass

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     */
    public void demarrerEnregistrementReleve() throws RemoteException; // TODO : modifier DiagCLass

}
