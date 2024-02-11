import Gestionnaire.Gestionnaire;

import java.rmi.Naming;

public class MainIOT {

    public static void main(String args[]) {

        // Récupération d'un proxy sur l'objet
        Gestionnaire leGestionnaire = (Gestionnaire) Naming.lookup("rmi://localhost:1099/LeGestionnaire");
        // Appel d'une méthode sur l'objet distant
        // ...


        /* Création de 5 capteurs, enregistrement des capteurs et démarrage des relevés */




    }
}
