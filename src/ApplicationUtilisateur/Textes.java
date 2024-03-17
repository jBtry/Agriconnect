package ApplicationUtilisateur;

/**
 * Cette classe contient tous les messages qui seront
 * affichés à utilisateur
 */
public abstract class Textes {

    /** Message d'accueil */
    protected static final String ACCUEIL = """
            ***************************************************
            *            Bienvenue sur Agriconnect !          *
            ***************************************************""";

    /** Message de fermeture */
    protected static final String ABIENTOT = "A Bientôt";


    /** Menu des fonctionnalités */
    protected static final String FCT =
            """
                    ----------------------CAPTEUR---------------------
                    1 - Ajouter un capteur
                    2 - Démarrer un capteur
                    3 - Stopper un capteur
                    4 - Retirer un capteur
                    5 - Lister les capteurs
                    6 - Voir le dernier relevé d'un capteur
                    7 - Obtenir des statistiques sur les relevés (moyenne et tendances)
                    8 - Modifier l'intervalle de mesure pour un capteurs
                    9 - Modifier l'intervalle de mesure pour tous les capteurs
                    ----------------------ACTIONNEUR------------------
                    10 - Ajouter un actionneur
                    11 - Retirer un actionneur
                    12 - Déclencher l'arrosage
                    13 - Obtenir l'état de l'arrosage
                    14 - Lister les actionneurs
                    ----------------------Quitter---------------------
                    15 - Quitter Agriconnect""";

    /** Demande de faire un choix à l'utilisateur */
    protected static final String CHOIX = "Que souhaitez-vous faire ?";

    /** Message d'erreur lors de la saisie utilisateur dans le menu des fonctionnalités */
    protected static final String ERR_SAISIE_FCT = "ERREUR - Vous devez saisir un nombre compris entre 1 et 15 !";

    /** Message pour la saisie d'un identifiant de capteur */
    protected static final String CAPTEUR = "Saisissez l'identifiant du capteur" ;

    /** Message pour la saisie d'un identifiant d'actionneur */
    protected static final String ACTIONNEUR = "Saisissez l'identifiant de l'actionneur" ;

    /** Message pour la saisie du nouvel intervalle de mesure */
    protected static final String INTERVALLE = "Saisissez le nouvelle intervalle de mesure (EN SECONDE)" ;

    /** Message d'erreur lors de la saisie utilisateur pour le choix de l'intervalle */
    protected static final String ERR_SAISIE_INTERVALLE = "ERREUR - Vous devez saisir un nombre !";

    /** Message pour la saisie de la durée (statistiques) */
    protected static final String DUREE_STATS = """
            Choisissez la durée sur laquelle vous souhaitez connaître les statistiques (moyenne et tendances)
            1 - Sur les dernières 24h
            2 - Sur la dernière heure""";

    /** Message pour la saisie de la durée d'arrosage */
    protected static final String DUREE_ARROSAGE = "Saisissez la durée de l'arrosage souhaitée (EN MINUTE)";

    /** Message d'erreur lors de la saisie de la durée (statistiques) */
    protected static final String ERR_SAISIE_DUREE_STATS = "ERREUR - Vous devez saisir le chiffre 1 ou 2 !";

    /** Message d'erreur lors de la saisie de l'arrosage */
    protected static final String ERR_SAISIE_DUREE_ARROSAGE = "ERREUR - Vous devez saisir un nombre";

    /** Message d'erreur lors de la propagation d'une exception */
    protected static final String EXCEPTION = "ERREUR - Veuillez redémarrez l'application";


}
