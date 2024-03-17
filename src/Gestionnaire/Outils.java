package Gestionnaire;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe contenant des outils utilisée par la classe GestionnaireImpl
 */
public abstract class Outils {

    /**
     * Calcule une moyenne à partir des retours d'une base de données (ResultatSet)
     * @param donnees retourné par la base de données
     * @return la moyenne des résultats, ou NaN si pas de relevés trouvés par la base de donnée.
     */
    protected static float moyenne(ResultSet donnees) throws SQLException {
        float somme = 0; float nombreReleve = 0; // nécessaire pour la moyenne
        while (donnees.next()) {
            float temperatureReleve = donnees.getFloat(1);
            somme += temperatureReleve;
            nombreReleve++;
        }

        return nombreReleve == 0 ? Float.NaN : somme/nombreReleve; // pas de relevé ? on retourne NaN sinon on retourne la moyenne
    }

    /**
     * Permet de vérifier si un actionneur est enregistré dans la BD.
     * Si l'actionneur n'est pas enregistré dans la BD, on ne peut pas le contrôler
     * On le considère comme inconnu
     * @param idActionneur l'identifiant de l'actionneur que l'on recherche
     * @param c objet représentant la connexion à la base de donnée
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     */
    protected static boolean estCeQueLActionneurEstEnregistre(String idActionneur, Connection c) throws SQLException {
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.EXISTENCE_ACTIONNEUR); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, idActionneur);
        ResultSet retourSQL = instructions.executeQuery();
        return retourSQL.next() ? true : false;  // Vérifier si l'actionneur existe (retourSQL.next() retourne True si au moins une ligne est retournée)
    }

    /**
     * Permet de vérifier si un capteur est enregistré dans la BD.
     * Si le capteur n'est pas enregistré dans la BD, on n'enregistre pas ses relevés et on ne peut pas le contrôler.
     * On le considère comme inconnu
     * @param idCapteur l'identifiant du capteur que l'on recherche
     * @param c objet représentant la connexion à la base de donnée
     * @throws SQLException si erreur lors de la recherche dans la base de données.
     */
    protected static boolean estCeQueLeCapteurEstEnregistre(String idCapteur, Connection c) throws SQLException {
        PreparedStatement instructions = c.prepareStatement(RequeteSQL.EXISTENCE_CAPTEUR); // On utilise PreparedStatement pour éviter les injections SQL
        instructions.setString(1, idCapteur);
        ResultSet retourSQL = instructions.executeQuery();
        return retourSQL.next() ? true : false;  // Vérifier si le capteur existe (retourSQL.next() retourne True si au moins une ligne est retournée)
    }


}
