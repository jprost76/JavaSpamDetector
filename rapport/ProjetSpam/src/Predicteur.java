
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

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
			while ((i < NB_MOTS_SIGNIFICATIFS) && (distJeton <= listeDistance[i])){
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
			//System.out.println(listeProba[i]);
			prodSpam = prodSpam * listeProba[i];
			prodHam = prodHam * (1-listeProba[i]); 
		}
	return prodSpam/(prodSpam+prodHam);
	}
	
	/**
	 * retourne une map trié des jetons du mail passé en paramètre dans l'ordre de significavité
	 * @param mail
	 * @return
	 */
	public TreeSet<String> jetonsTriees(ArrayList<String> mail) {
		TreeSet<String> map = new TreeSet<String>(new Comparateur(tproba));
		map.addAll(mail);
		return map;
	}
	
	/** affiche les jetons du mail passé en paramètre
	 *  du plus significatif au moins significatif, et la proba associé**/
	public void AfficherJetonsTriees(ArrayList<String> mail) {
		TreeSet<String> jetons = this.jetonsTriees(mail);
		Iterator<String> it = jetons.descendingIterator();
		while (it.hasNext()) {
			String j = it.next();
			float pj = tproba.getOrDefault(j, DEFAULT_PROB);
			try {
				System.out.printf(String.format("%-30s : %-10f\n",j,pj));
			}
			catch (Exception ex) {
				System.out.print(j+"    :     "+pj);
			}
		}
	}
	
	public TableProba getTProba() {
		return tproba;
	}
	
	/** compare les jetons selon leur "significativité". 
	 * plus la probabilité d'un jeton est proche de 1 où de 0,
	 * plus le jeton est significatif
	 * @author jprost
	 *
	 */
	class Comparateur implements Comparator<String> {
		
		private TableProba table;
		
		public Comparateur(TableProba uneTable){
			super();
			table = uneTable;
		}
		
		public int compare(String j1, String j2) {
			float p1 = table.getOrDefault(j1, DEFAULT_PROB);
			float p2 = table.getOrDefault(j2, DEFAULT_PROB);
			double d1 = Math.abs(0.5-p1);
			double d2 = Math.abs(0.5-p2);
			return (d1>d2 ? 1 : (d1 == d2 ? 0 : -1));
		}
	}
}
