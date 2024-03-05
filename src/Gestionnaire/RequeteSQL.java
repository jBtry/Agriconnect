package Gestionnaire;


/**
 * Contient toutes les requêtes SQL
 * utilisées par le Gestionnaire pour donner
 * des ordres à la base de données
 */
public abstract class RequeteSQL {

    private static final String NOM_TABLE_CAPTEUR = "capteurs";
    private static final String NOM_TABLE_RELEVE = "releves";

    /** Requête SQL permettant de créer la table contenant la liste des capteurs, si celle-ci n'existe pas deja */
    protected static final String CREATION_TABLE_CAPTEURS = "CREATE TABLE IF NOT EXISTS capteurs"
            + "(Id varchar(10) PRIMARY KEY,"
            + " Latitude DECIMAL(8,6) NOT NULL,"
            + " Longitude DECIMAL(9,6) NOT NULL"
            + ");";

    /** Requête SQL permettant de créer la table contenant la liste des relevés, si celle-ci n'existe pas deja */
    protected static final String CREATION_TABLE_RELEVES = "CREATE TABLE IF NOT EXISTS releves"
            + "(IdCapteur varchar(10) NOT NULL,"
            + " Temperature DECIMAL(3,1) NOT NULL,"
            + " TauxHumidite DECIMAL(3,1) NOT NULL,"
            + " Horodatage DATETIME NOT NULL,"
            + " PRIMARY KEY (IdCapteur, Horodatage)"
            + ");";

    /** Requête SQL permettant d'ajouter un capteur dans la table CAPTEUR */
    protected static final String INSERTION_CAPTEUR = "INSERT INTO "+NOM_TABLE_CAPTEUR+" VALUES(?,?,?)";

    /** Requête SQL permettant de supprimer un capteur dans la table CAPTEUR */
    protected static final String SUPPRESSION_CAPTEUR = "DELETE FROM "+NOM_TABLE_CAPTEUR+" WHERE id=?";

    /** Requête SQL permettant d'insérer un relevé d'un capteur dans la table contenant les relevés */
    protected static final String INSERTION_RELEVE= "INSERT INTO "+NOM_TABLE_RELEVE+" VALUES(?,?,?,?)";

    /** Requête SQL permettant de lister les capteurs */
    protected static final String SELECT_CAPTEUR= "SELECT * FROM "+NOM_TABLE_CAPTEUR;

    /** Requête SQL permettant de vérifier si un capteur existe */
    protected static final String EXISTENCE_CAPTEUR= "SELECT * FROM "+NOM_TABLE_CAPTEUR+" WHERE id=?";

    /** Requête SQL permettant de récupérer le dernier relevé d'un capteur donné */
    protected static final String DERNIERE_INFO_CAPTEUR= "SELECT TOP 1 * FROM "+NOM_TABLE_RELEVE+" WHERE id=? ORDER BY DESC";
}
