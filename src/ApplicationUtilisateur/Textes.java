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

    /** Message de fermeture */
    protected static final String ABIENTOT = "A Bientôt";


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
    protected static final String CHOIX = "Que souhaitez-vous faire ?";

    /** Message d'erreur lors de la saisie utilisateur dans le menu des fonctionnalités */
    protected static final String ERR_SAISI_FCT = "ERREUR - Vous devez saisir un nombre compris entre 1 et 10 !";

    /** Message pour la saisie d'un identifiant de capteur */
    public static final String CAPTEUR = "Saisissez l'identifiant du capteur" ;

    /** Message pour la saisie du nouvelle intervalle de mesure */
    public static final String INTERVALLE = "Saisissez le nouvelle intervalle de mesure (EN SECONDE)" ;

    /** Message d'erreur lors de la saisie utilisateur pour le choix de l'intervalle */
    public static final String ERR_SAISI_INTERVALLE = "ERREUR - Vous devez saisir un nombre !";

    /** Message pour la saisie de la durée (statistiques) */
    public static final String DUREE = "Choisissez la durée sur laquelle vous souhaitez connaître les statistiques (moyenne et tendances)\n"
                                        + "1 - Sur les dernières 24h\n"
                                        + "2 - Sur la dernière heure";

    /** Message d'erreur lors de la saisie de la durée (statistiques) */
    protected static final String ERR_SAISI_DUREE = "ERREUR - Vous devez saisir le chiffre 1 ou 2 !";


}
