package IoT;

import Gestionnaire.GestionnaireImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public abstract class MainIOT {

    /**
     * Crée le registre RMI (s'il n'existe pas deja),
     * crée 6 capteurs et 2 actionneurs et enregistre ceux-ci dans le registre RMI
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
        /* Crée 6 capteurs, 2 actionneurs et enregistre ceux-ci dans le registre RMI */
        try {
            String idCapteur = "C";
            String idActionneur = "A";

            /* Création des 6 capteurs */
            for(int i=1; i <= 6; i++) {
                CapteurImpl unCapteur = new CapteurImpl(idCapteur+i,GPS.listeGPSCapteurs.get(i-1)[0], GPS.listeGPSCapteurs.get(i-1)[1]);
                Naming.rebind(idCapteur+i, unCapteur);
                System.out.println("Le Capteur numéro " + i + " a été créé et enregistré dans le registre RMI, voici ses attributs : "+"\n"+unCapteur);
            }

            /* Création des 2 actionneurs */
            for(int i=1; i <= 2; i++) {
                ActionneurImpl unActionneur = new ActionneurImpl(idActionneur+i,GPS.listeGPSActionneurs.get(i-1)[0], GPS.listeGPSActionneurs.get(i-1)[1]);
                Naming.rebind(idActionneur+i, unActionneur);
                System.out.println("L'Actionneur numéro " + i + " a été créé et enregistré dans le registre RMI, voici ses attributs : "+"\n"+unActionneur);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

