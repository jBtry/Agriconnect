package IoT;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contient les coordonnées GPS des actionneurs et des capteurs
 * créés dans la classe MainIOT.
 */
public abstract class GPS {

    /*
     * À partir de la troisième décimale, la précision est autour de 100m.
     * On fait donc varier après la troisième décimale afin qu'un actionneur soit dans la même zone
     * que trois capteurs.
     */

    /* Zone 1 */
    protected static final float[] gpsActionneurUn = {43.255594F, 2.197931F};
    protected static final float[] gpsCapteurUn = {43.255576F, 2.197868F};
    protected static final float[] gpsCapteurDeux = {43.2553739F, 2.197073F};
    protected static final float[] gpsCapteurTrois = {43.255566F, 2.197272F};

    /* Zone 2 */
    protected static final float[] gpsActionneurDeux = {42.671257F, 2.693490F};
    protected static final float[] gpsCapteurQuatre = {42.671175F, 2.693765F};
    protected static final float[] gpsCapteurCinq = {42.671712F, 2.693040F};
    protected static final float[] gpsCapteurSix = {42.671828F, 2.693375F};

    /* Liste récapitulative des coordonnées GPS des capteurs */
    protected static final ArrayList<float[]> listeGPSCapteurs = new ArrayList<>(Arrays.asList(gpsCapteurUn,
                                                                                               gpsCapteurDeux,
                                                                                               gpsCapteurTrois,
                                                                                               gpsCapteurQuatre,
                                                                                               gpsCapteurCinq,
                                                                                               gpsCapteurSix));

    /* Liste récapitulative des coordonnées GPS des actionneurs */
    protected static final ArrayList<float[]> listeGPSActionneurs = new ArrayList<>(Arrays.asList(gpsActionneurUn, gpsActionneurDeux));
}
