package IoT;

import java.rmi.RemoteException;

/**
 * Méthode de capteur accessible à distance
 * Hérite de l'interface ObjetConnecte qui, elle-même, hérite de l'interface java.rmi.remote
 */
public interface Capteur extends ObjetConnecte {

    /**
     * Modifie l'intervalle entre deux relevés
     * @param intervalle entier représentant la valeur de l'intervalle entre deux relevés (en seconde)
     * @throws RemoteException
     */
    public void modifierIntervalle(int intervalle) throws RemoteException;

    /**
     * Démarre les relevés
     * (intervalle de 5 secondes par défaut)
     * @throws RemoteException
     */
    public void demarrerEnregistrementReleve() throws RemoteException;

    /**
     * Méthode permettant d'influer sur le taux humidité
     * Utile pour simuler l'impact de l'arrosage
     * @throws RemoteException
     */
    public void influerTauxHumidite() throws RemoteException;
}
