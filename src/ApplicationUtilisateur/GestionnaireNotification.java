package ApplicationUtilisateur;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Met à disposition des méthodes accessibles à distance
 * pour notifier l'utilisateur de l'application Agriconnect d'un évènement.
 */
public interface GestionnaireNotification extends Remote {

    /**
     * Permet de notifier l'application utilisateur d'un évènement
     * @param event chaine de caractère décrivant l'évènement.
     * @throws java.rmi.RemoteException si erreur lors de la communication.
     */
    public void notification(String event) throws RemoteException;
}
