package IoT;

import java.rmi.RemoteException;

/**
 * Méthode accessible à distance pour un Capteur
 * Hérite de l'interface ObjetConnecte qui, elle-même, hérite de l'interface java.rmi.remote
 */
public interface Capteur extends ObjetConnecte {

    /**
     * Modifie l'intervalle entre deux relevés
     * @param intervalle entier représentant la valeur de l'intervalle entre deux relevés (en seconde)
     * @throws RemoteException si erreur lors de la communication.
     */
    public void modifierIntervalle(int intervalle) throws RemoteException;

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     */
    public void demarrerEnregistrementReleve() throws RemoteException;

    /**
     * Méthode permettant d'influer sur le taux humidité
     * Utile pour simuler l'impact de l'arrosage
     */
    void influerTauxHumidite() throws RemoteException;
}
