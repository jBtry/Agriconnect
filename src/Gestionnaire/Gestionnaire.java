package Gestionnaire;

public interface Gestionnaire extends java.rmi.Remote {

    /**
     * Permet d'enregistrer un capteur dans la base de donnée du gestionnaire contenant la liste des capteurs.
     * @param gps tableau contenant la position du capteur (latitude et longitude)
     * @return une chaine de caractère indiquant au capteur si l'enregistrement a réussi.
     */
    public String enregistrerCapteur(String idCapteur, float[] gps);

    /**
     * Permet de retirer un capteur dans la base de donnée du gestionnaire contenant la liste des capteurs.
     * @param id chaine de caractère identifiant le capteur ayant fait le relevé.
     * @return une chaine de caractère indiquant au capteur s'il a bien été retiré.
     */
    public String retirerCapteur(String id);


    /**
     * Permet d'enregistrer les informations dans la Base de données contenant les valeurs relevées par les capteurs.
     * @param id  chaine de caractère permettant d'identifier le capteur ayant fait le relevé.
     * @param temp flottant représentant la température relevé par le capteur.
     * @param tauxH flottant représentant le taux d'humidité relevé par le capteur.
     */
    public void enregistrerValeur(String id, float temp, float tauxH);

}
