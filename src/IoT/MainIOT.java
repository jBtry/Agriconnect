package IoT;

import Gestionnaire.GestionnaireImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public abstract class MainIOT {

    /**
     * Crée le registre RMI (s'il n'existe pas deja),
     * crée 6 capteurs et deux actionneurs et enregistre ceux-ci dans le registre RMI
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
        /* Crée 10 capteurs et enregistre ceux-ci dans le registre RMI */
        try {
            String idCapteur = "C";
            for(int i=1; i <= 10; i++) {
                CapteurImpl unCapteur= new CapteurImpl(idCapteur+i,Outils.genererLatitudeAleatoires(), Outils.genererLongitudeAleatoires());
                Naming.rebind(idCapteur+i, unCapteur);
                System.out.println("Le Capteur numéro " + i + " a été créé et enregistré dans le registre RMI, voici ses attributs : "+"\n"+unCapteur);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

