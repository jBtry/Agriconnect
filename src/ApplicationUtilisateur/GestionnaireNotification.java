package ApplicationUtilisateur;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Met à disposition les méthodes accessibles à distance
 * pour notifier l'utilisateur de l'application Agriconnect d'un évènement.
 */
public interface GestionnaireNotification extends Remote {
    // TODO : expliquer que par la suite on peut se servir de ça pour ajouter d'autre fct ....
    /**
     * Permet de notifier l'application utilisateur d'un évènement
     * @param event chaine de caractère décrivant l'exception.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void notification(String event) throws RemoteException;
}
