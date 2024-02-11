import Gestionnaire.GestionnaireImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MainGestionnaire {

    public static void main(String args[]) {

        // Démarre le RMIregistry
        LocateRegistry.createRegistry(1099);
        GestionnaireImpl leGestionnaire = new GestionnaireImpl();
        Naming.rebind("LeGestionnaire", leGestionnaire);
        System.out.println("LeGestionnaire a été créée et déclaré auprès du serveur de noms");

    }

}
