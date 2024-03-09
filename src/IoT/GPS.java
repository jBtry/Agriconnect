package IoT;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contient les coordonnées GPS des actionneurs et des capteurs
 * créés dans la classe MainIOT.
 */
public abstract class GPS {

    /* À partir de la troisième décimale, la précision est autour de 100m.
     * On fait donc varier après la troisième décimale afin qu'un actionneur soit dans la même zone
     * que trois capteurs.
     */

    /* Zone 1 */
    protected static final float[] gpsActionneurUn = {43.25559455064179F, 2.1979315888215214F};
    protected static final float[] gpsCapteurUn = {43.25557673597238F, 2.197868717022648F};
    protected static final float[] gpsCapteurDeux = {43.25537390428052F, 2.197073181933987F};
    protected static final float[] gpsCapteurTrois = {43.25556685369287F, 2.1972723988140626F};

    /* Zone 2 */
    protected static final float[] gpsActionneurDeux = {42.671257221872615F, 2.693490736082865F};
    protected static final float[] gpsCapteurQuatre = {42.67117582072808F, 2.693765996305087F};
    protected static final float[] gpsCapteurCinq = {42.67171205114392F, 2.6930402509392385F};
    protected static final float[] gpsCapteurSix = {42.67182898636013F, 2.6933751154983643F};

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
