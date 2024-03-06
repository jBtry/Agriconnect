package Gestionnaire;

import Gestionnaire.GestionnaireImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

public abstract class MainGestionnaire {


    /**
     * Crée le registre RMI (s'il n'existe pas deja), ensuite crée un objet gestionnaire
     * et enregistre celui-ci.
     * @param args non utilisés
     */
    public static void main(String args[]) {
        /* Crée le registre RMI (s'il n'existe pas deja) */
        try {
            LocateRegistry.createRegistry(1099);
        } catch(java.rmi.server.ExportException e) {
            ; // Un registre RMI existe deja sur le port 1099 ... on ne fait donc rien
        } catch (RemoteException err) {
            System.out.println("Erreur lors de la création du registre");
        }

        try {
            GestionnaireImpl leGestionnaire = new GestionnaireImpl();
            Naming.rebind("LeGestionnaire", leGestionnaire);
            System.out.println("Le Gestionnaire a été créée et déclaré auprès du serveur de noms"
                               +"\n"+leGestionnaire);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
