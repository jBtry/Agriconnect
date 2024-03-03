package Gestionnaire;

import Gestionnaire.GestionnaireImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

public class MainGestionnaire {


    /**
     * Crée le registre RMI, ensuite crée un objet gestionnaire
     * et enregistre celui-ci.
     * @param args non utilisés
     */
    public static void main(String args[]) {
    // TODO : Mieux gérer les exceptions dans les prochaines versions ...
        try {
            LocateRegistry.createRegistry(1099);
            GestionnaireImpl leGestionnaire = new GestionnaireImpl();
            Naming.rebind("LeGestionnaire", leGestionnaire);
            System.out.println("Le Gestionnaire a été créée et déclaré auprès du serveur de noms"
                               +"\n"+leGestionnaire.toString());
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
