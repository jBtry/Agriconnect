package IoT;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Méthode d'Actionneur accessible à distance
 * Hérite de l'interface ObjetConnecte qui, elle-même, hérite de l'interface java.rmi.remote
 */
public interface Actionneur extends ObjetConnecte {

    /**
     * Permet d'ordonner à un actionneur de déclencher l'arrosage.
     * @param duree entier représentant la durée d'arrosage en MINUTE
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public void declencherArrosage(int duree) throws RemoteException, MalformedURLException, NotBoundException;

    /**
     * @return le temps restant avant la fin de l'arrosage en SECONDE
     * @throws RemoteException
     */
    public int obtenirTempsRestantArrosage() throws RemoteException;


}
