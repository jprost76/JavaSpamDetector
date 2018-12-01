
import java.util.ArrayList;
import java.util.Iterator;

public class Predicteur {
	
	/** HashTable associant a chaque mot sa probabilite d'appartenir a un spam */
	private TableProba tproba;
	
	/** le nombre de mots significatifs releves dans le mail */
	public static final int NB_MOTS_SIGNIFICATIFS = 15;
	
	/** probabilite donne a un mot jamais vu avant d'appartenir à un spam */
	public static final float DEFAULT_PROB = (float)0.4;
	
	/**
	 * Constructeur de l'objet
	 * @param t TableProba
	 */
	public Predicteur(TableProba t){
		tproba = t;
	}
	
	public void setTProba(TableProba t){
		tproba = t;
	}
		
	/**
	 * recherche les NB_MOTS proba les plus significatives, 
	 * et en déduit la probabilité que le mail soit un spam
	 * @param mail
	 * @return
	 */
	public float probaSpam(ArrayList<String> mail){
		float[] listeDistance = new float[NB_MOTS_SIGNIFICATIFS];
		float[] listeProba = new float[NB_MOTS_SIGNIFICATIFS];
		Iterator<String> it = mail.iterator();
		while (it.hasNext()){
// recherche de la probablité associé au jeton
			String jeton = it.next();
			float probaJeton;
			if (tproba.estPresent(jeton)){
				probaJeton = tproba.obtenirProba(jeton);
			} else {
				probaJeton = DEFAULT_PROB;
			}
// insertion de de la proba du jeton dans la liste des proba significatives
			float distJeton = (float)Math.abs(0.5-probaJeton);
			int i = 0;
			while (i < NB_MOTS_SIGNIFICATIFS & distJeton <= listeDistance[i]){
				i++;
			}
			if (i < NB_MOTS_SIGNIFICATIFS){
				// insertion de distJeton dans listeDistance au rang i
				float tmp1 = distJeton;
				float tmp2;
				for (int j=i;j<NB_MOTS_SIGNIFICATIFS;j++) {
					tmp2 = listeDistance[j];
					listeDistance[j] = tmp1;
					tmp1 = tmp2;
				}
				// insertion de probaJeton dans listeProba au rang i
				tmp1 = probaJeton;
				for (int j=i;j<NB_MOTS_SIGNIFICATIFS;j++) {
					tmp2 = listeProba[j];
					listeProba[j] = tmp1;
					tmp1 = tmp2;
				}
			}	
		}
// calcul de la probabilite en utilisant les proba les plus significatives
		float prodSpam = 1;
		float prodHam = 1;
		for (int i=0;i<NB_MOTS_SIGNIFICATIFS;i++){
			prodSpam = prodSpam * listeProba[i];
			prodHam = prodHam * (1-listeProba[i]); 
		}
	return prodSpam/(prodSpam+prodHam);
	}
}
