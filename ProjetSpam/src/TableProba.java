import java.util.HashMap;
import java.util.Iterator;

/**
 * Cette classe reperesente une table qui a chaque jeton associe une probabilité
 * entre 0 et 1 que un mail contenant ce jeton soit un spam
 * @author jprost
 *
 */
public class TableProba extends HashMap<String,Float>{
/**
 * cree une table associant à chaque jeton le probabilite
 * que un mail contenant ce jeton soit un spam
 * @param tableSpam : occurence des jetons dans les spam
 * @param tableHam : occurence des jetons dans les ham
 */
	TableProba(TableOccur tableSpam, TableOccur tableHam){
		super();
		int NbSpam = tableSpam.getNbmail();
		int NbHam = tableHam.getNbmail();
		Iterator<HashMap.Entry<String, Integer>> it = tableSpam.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<String, Integer>  pair = it.next();
			String jeton = pair.getKey();
			Integer occSpam = pair.getValue();
//pjs : prob que le jeton appartienne au mail sachant que c'est un spam
			float pjs = Math.min(1,occSpam/NbSpam);
			Integer occHam = tableHam.obtenirOccurence(jeton);
			float pjh;
			if (occHam != null) {
				pjh = Math.min(1,occSpam/NbSpam);
			} else { pjh = 0;}
			this.put(jeton,pjs/(pjs+pjh));
		}
	}
	
	public float obtenirProba(String pmot) {
		return this.get(pmot);
	}
	
	public boolean estPresent(String pmot) {
		return this.containsKey(pmot);
	}
	
}
