import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Cette classe reperesente une table qui a chaque jeton associe une probabilité
 * entre 0 et 1 que un mail contenant ce jeton soit un spam
 * @author jprost
 *
 */
@SuppressWarnings("serial")
public class TableProba extends HashMap<String,Float> implements Serializable {
	
/**nombre minimum d'occurences du mot dans les 2 corpus pour calculer la probabilité **/
	public final int LIM_INF_MOTS = 5;
	
/** probabilité minimale attribuable à un jeton **/
	public final float PROBA_MIN = (float)0.02;
	
/** probabilité maximale attribuable à un jeton **/
	public final float PROBA_MAX = (float)0.99;
	
/** biais par lequel on multiplie le nombre d'occurences dans le corpus non spam pour limité les faux positifs **/
	public final int BIAIS = 2;

	
/**
 * cree une table associant à chaque jeton contenu dans une des 2 tables passées en entrée
 *  le probabilité que un mail contenant ce jeton soit un spam
 * @param tableSpam : occurence des jetons dans les spam
 * @param tableHam : occurence des jetons dans les ham
 */
	public TableProba(TableOccur tableSpam, TableOccur tableHam) throws TableNonInitException{
		super();
		if (tableSpam == null || tableHam == null) {
			throw new TableNonInitException();
		} else {
			int NbSpam = tableSpam.getNbmail();
			int NbHam = tableHam.getNbmail();
	//on parcourt tous les mots de tableSpam
			Iterator<HashMap.Entry<String, Integer>> it = tableSpam.entrySet().iterator();
			while (it.hasNext()) {
				HashMap.Entry<String, Integer>  pair = it.next();
				String jeton = pair.getKey();
				Integer occSpam = pair.getValue();
				int occHam = 0;
				if (tableHam.estPresent(jeton)) {
					occHam = BIAIS * tableHam.obtenirOccurence(jeton);
				} 
				if (occHam + occSpam >= LIM_INF_MOTS) {
					//pjs : prob que le jeton appartienne au mail sachant que ce mail est un spam
					float pjs = Math.min(1.f,(float)occSpam/NbSpam);	
					//pjh : prob que le jeton appartienne au mail sachant que ce mail est un ham
					float pjh = Math.min(1.f,(float)occHam/NbHam);
					float psj = Math.max( Math.min(pjs/(pjs+pjh), PROBA_MAX) , PROBA_MIN ); 
					this.put(jeton,psj);
				}
			}
	//on parcourt tous les mots de tableHam, on traite ceux qui n'ont pas déjà été traité
			Iterator<HashMap.Entry<String, Integer>> it2 = tableHam.entrySet().iterator();
			while (it2.hasNext()) {
				HashMap.Entry<String, Integer>  pair = it2.next();
				String jeton = pair.getKey();
				Integer occHam = BIAIS * pair.getValue(); 
				if (!this.containsKey(jeton)) {
					int occSpam = 0;
					if (tableSpam.estPresent(jeton)) {
						occSpam = tableSpam.obtenirOccurence(jeton);
					}
					if ( occSpam + occHam >= LIM_INF_MOTS ) {
						//pjs : prob que le jeton appartienne au mail sachant que ce mail est un spam
						float pjs = Math.min(1.f,(float)occSpam/NbSpam);	
						//pjh : prob que le jeton appartienne au mail sachant que ce mail est un ham
						float pjh = Math.min(1.f,(float)occHam/NbHam);
						float psj = Math.max( Math.min(pjs/(pjs+pjh), PROBA_MAX) , PROBA_MIN ); 
						this.put(jeton,psj);
					}
				}
			}
		}
			

	}
	
	/** retourne une probabilité entre 0 et 1 que un mail contenant 
	 * le jeton passé en paramètre soit un spam **/
	public float obtenirProba(String pmot) {
		return this.get(pmot);
	}
	
	/**
	 * Indique si le mot passé en paramètre est présent dans la table
	 * @param pmot
	 * @return true si le mot est présent, false sinon
	 */
	public boolean estPresent(String pmot) {
		return this.containsKey(pmot);
	}
	
	/** enregistre la table dans le fichier passé en paramètre **/
	public void save(File fic) {
		try {
			FileOutputStream flux = new FileOutputStream(fic);
			ObjectOutputStream writer = new ObjectOutputStream(flux);
			writer.writeObject(this);
			writer.close();
			System.out.println("table enregistrée dans "+fic.getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
		
}
