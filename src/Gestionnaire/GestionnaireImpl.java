package Gestionnaire;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Gestionnaire.ConnexionBD.connexion;
import static Gestionnaire.RequeteSQL.*;

/**
 * Gère une liste des capteurs associés et enregistre leurs relevés
 */
public class GestionnaireImpl extends UnicastRemoteObject implements Gestionnaire {

    /** Contient la connexion/session avec la base de données afin d'effectuer des opérations sur celle-ci */
    private Connection c;

    /** Un gestionnaire est caractérisé par un nom */
    private String nom;

    public GestionnaireImpl() throws RemoteException, SQLException, ClassNotFoundException {
        super();
        this.c = connexion();
        nom = "Gestionnaire Agriconnect";
    }

    @Override
    public String toString() {
        return "GestionnaireImpl{" +
                "nom='" + nom + '\'' +
                '}';
    }


}
