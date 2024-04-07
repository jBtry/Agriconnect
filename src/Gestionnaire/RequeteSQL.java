package Gestionnaire;


/**
 * Contient toutes les requêtes SQL
 * utilisées par le Gestionnaire pour donner
 * des ordres à la base de données
 */
public abstract class RequeteSQL {

    /** Nom de la table contenant les capteurs */
    private static final String NOM_TABLE_CAPTEUR = "capteurs";
    /** Nom de la table contenant les relevés */
    private static final String NOM_TABLE_RELEVE = "releves";
    /** Nom de la table contenant les actionneurs */
    private static final String NOM_TABLE_ACTIONNEURS = "actionneurs";

    /** Requête SQL permettant de créer la table contenant la liste des capteurs, si celle-ci n'existe pas deja */
    protected static final String CREATION_TABLE_CAPTEURS =
            "CREATE TABLE IF NOT EXISTS " + NOM_TABLE_CAPTEUR
            + "(Id varchar(10) PRIMARY KEY,"
            + " Latitude REAL NOT NULL,"
            + " Longitude REAL NOT NULL"
            + ");";

    /** Requête SQL permettant de créer la table contenant la liste des relevés, si celle-ci n'existe pas deja */
    protected static final String CREATION_TABLE_RELEVES =
            "CREATE TABLE IF NOT EXISTS " + NOM_TABLE_RELEVE
            + "(IdCapteur varchar(10) NOT NULL,"
            + " Temperature REAL NOT NULL,"
            + " TauxHumidite REAL NOT NULL,"
            + " Horodatage DATETIME NOT NULL,"
            + " PRIMARY KEY (IdCapteur, Horodatage)"
            + ");";

    /** Requête SQL permettant de créer la table contenant la liste des actionneurs, si celle-ci n'existe pas deja */
    protected static final String CREATION_TABLE_ACTIONNEURS =
            "CREATE TABLE IF NOT EXISTS " + NOM_TABLE_ACTIONNEURS
            + "(Id varchar(10) PRIMARY KEY,"
            + " Latitude REAL NOT NULL,"
            + " Longitude REAL NOT NULL,"
            + " DernierArrosage DATETIME,"
            + " Duree NUMBER SMALLINT" // durée sauvegardée en seconde
            + ");";

    /** Requête SQL permettant d'ajouter un capteur dans la table ACTIONNEUR */
    protected static final String INSERTION_ACTIONNEUR = "INSERT INTO "+NOM_TABLE_ACTIONNEURS+"(id, Latitude, Longitude) VALUES(?,?,?)";

    /** Requête SQL permettant d'ajouter un capteur dans la table CAPTEUR */
    protected static final String INSERTION_CAPTEUR = "INSERT INTO "+NOM_TABLE_CAPTEUR+" VALUES(?,?,?)";

    /** Requête SQL permettant de supprimer un capteur dans la table ACTIONNEUR */
    protected static final String SUPPRESSION_ACTIONNEUR = "DELETE FROM "+NOM_TABLE_ACTIONNEURS+" WHERE id=?";

    /** Requête SQL permettant de supprimer un capteur dans la table CAPTEUR */
    protected static final String SUPPRESSION_CAPTEUR = "DELETE FROM "+NOM_TABLE_CAPTEUR+" WHERE id=?";

    /** Requête SQL permettant d'insérer un relevé d'un capteur dans la table contenant les relevés */
    protected static final String INSERTION_RELEVE= "INSERT INTO "+NOM_TABLE_RELEVE+" VALUES(?,?,?,?)";

    /** Requête SQL permettant de lister les capteurs */
    protected static final String SELECT_CAPTEUR= "SELECT * FROM "+NOM_TABLE_CAPTEUR;

    /** Requête SQL permettant de lister les capteurs */
    protected static final String SELECT_ACTIONNEUR= "SELECT Id, Latitude, Longitude FROM "+NOM_TABLE_ACTIONNEURS;

    /** Requête SQL permettant de vérifier si un capteur existe */
    protected static final String EXISTENCE_CAPTEUR= "SELECT * FROM "+NOM_TABLE_CAPTEUR+" WHERE id=?";

    /** Requête SQL permettant de vérifier si un actionneur existe */
    protected static final String EXISTENCE_ACTIONNEUR= "SELECT * FROM "+NOM_TABLE_ACTIONNEURS+" WHERE id=?";

    /** Requête SQL permettant d'obtenir les températures relevées sur une période  */
    protected static final String RELEVE_TEMPERATURE= "SELECT Temperature FROM "+NOM_TABLE_RELEVE+" WHERE Horodatage > datetime('now', ?);";

    /** Requête SQL permettant d'obtenir les températures relevées sur un intervalle de temps  */
    protected static final String RELEVE_TEMPERATURE_INTERVALLE =
            "SELECT Temperature FROM " + NOM_TABLE_RELEVE + " WHERE Horodatage > datetime('now', ?) AND Horodatage < datetime('now', ?);";

    /** Requête SQL permettant d'obtenir les taux d'humidités relevés sur une période */
    protected static final String RELEVE_TAUXHUMIDITE= "SELECT TauxHumidite FROM "+NOM_TABLE_RELEVE+" WHERE Horodatage > datetime('now', ?);";

    /** Requête SQL permettant d'obtenir les températures relevées sur un intervalle de temps  */
    protected static final String RELEVE_TAUXHUMIDITE_INTERVALLE =
            "SELECT TauxHumidite FROM " + NOM_TABLE_RELEVE + " WHERE Horodatage > datetime('now', ?) AND Horodatage < datetime('now', ?);";

    /** Requête SQL permettant de récupérer le dernier relevé d'un capteur donné */
    protected static final String DERNIERE_INFO_CAPTEUR= "SELECT * FROM "+NOM_TABLE_RELEVE+" WHERE idCapteur=? ORDER BY Horodatage DESC LIMIT 1";

    /** Requête SQL permettant de consulter le dernier état connu d'arrosage */
    protected static final String ETAT_ARROSAGE= "SELECT DernierArrosage, Duree FROM "+NOM_TABLE_ACTIONNEURS+" WHERE id=?";

    /** Requête SQL permettant de mettre à jour le dernier état connu d'arrosage */
    protected static final String INSERTION_ETAT_ARROSAGE= "UPDATE "+NOM_TABLE_ACTIONNEURS+" SET DernierArrosage=?, Duree=? WHERE id=?";
}
