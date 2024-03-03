package ApplicationUtilisateur;

/**
 * Cette classe contient tous les messages qui seront
 * affichés à utilisateur
 */
public abstract class Textes {

    /** Message d'accueil */
    protected static final String ACCUEIL = "***************************************************\n" +
                                         "*            Bienvenue sur Agriconnect !          *\n" +
                                         "***************************************************";

    /** Menu des fonctionnalités */
    protected static final String FCT = "1 - Ajouter un capteur\n" +
            "2 - Démarrer un capteur\n" +
            "3 - Stopper un capteur\n" +
            "4 - Retirer un capteur\n" +
            "5 - Lister les capteurs\n" +
            "6 - Voir le dernier relevé d'un capteur\n" +
            "7 - Obtenir des statistiques sur les relevés (moyenne et tendances)\n" +
            "8 - Modifier l'intervalle de mesure pour un capteurs\n" +
            "9 - Modifier l'intervalle de mesure pour tous les capteurs\n" +
            "10 - Quitter Agriconnect";

    /** Demande de faire un choix à l'utilisateur */
    protected static final String CHOIX = "Que souhaitez-vous faire ? ";

    /** Message d'erreur lors de la saisi utilisateur */
    protected static final String ERR_SAISI= "Vous devez saisir un nombre compris entre 1 et 10 ";

}
