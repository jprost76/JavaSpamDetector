import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

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
					//psj : prob que un mail soit un spam sachant que il contient le jeton
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
	
	/** Affiche n éléments de la table triée selon la probabilité, dans l'ordre décroissant **/
	public void AfficherTrier(int n) {
		TreeSet<String> jetonsTries = new TreeSet<String>(new Comparateur(this));
		jetonsTries.addAll(this.keySet());
		Iterator<String> it = jetonsTries.descendingIterator();
		int i = 0;
		while (it.hasNext()) {
			String j = it.next();
			if ( (i < (n/2)) || (i > jetonsTries.size()-n/2) ) {
				float prob = this.get(j);
				try {
					System.out.printf(String.format("%-30s : %-10f\n",j,prob));
				}
				catch (Exception ex) {
					System.out.print(j+"    :     "+prob);
				}
			}
			if (i == (int)(n/2)){
					System.out.println("...");
			}
			i++;
		}
	}
	
	/** classe permettant de comparer 2 jetons selon leur probabilité contenu dans la table en attribut **/
	class Comparateur implements Comparator<String> {
		
		private TableProba table;
		
		public Comparateur(TableProba uneTable){
			super();
			table = uneTable;
		}
		
		/** retourne 1 si p(j1)> p(j2), 0 si j1 a autant d'occurence que j2, -1 sinon **/
		public int compare(String j1, String j2) {
			float p1 = table.getOrDefault(j1, (float)0);
			float p2 = table.getOrDefault(j2, (float)0);
			return (p1>p2 ? 1 : (p1 == p2 ? 0 : -1));
		}
	}
}
