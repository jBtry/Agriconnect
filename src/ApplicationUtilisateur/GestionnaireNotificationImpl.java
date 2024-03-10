package ApplicationUtilisateur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *  Implémente les méthodes accessibles à distance ayant pour but de
 *  notifier l'utilisateur de l'application Agriconnect d'un évènement.
 */

public class GestionnaireNotificationImpl extends UnicastRemoteObject implements GestionnaireNotification {

    /**
     * Constructeur par défaut
     * @throws RemoteException si erreur lors de la communication.
     */
    protected GestionnaireNotificationImpl() throws RemoteException {
        super();
    }

    /**
     * Permet de notifier l'application utilisateur d'un évènement
     * @param event chaine de caractère décrivant l'exception.
     * @throws RemoteException si erreur lors de la communication.
     */
    @Override
    public void notification(String event) throws RemoteException {
        MainAppUser.afficher(event);
    }
}
