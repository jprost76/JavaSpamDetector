import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;



@SuppressWarnings("serial")
public class TableOccur extends HashMap<String,Integer> implements Serializable {
	int nbMail;
	
	TableOccur(){
		super();
		this.nbMail=0;
	}
	public int obtenirOccurence(String pmot) {
		return this.get(pmot);
	}
	
	public boolean estPresent(String pmot) {
		return this.containsKey(pmot);
	}
	public void setNbmail(int pnbmail) {
		this.nbMail=pnbmail;;
	}
	
	public int getNbmail() {
		return this.nbMail;
	}
	
	/**affiche les jetons dans la table et le nombre d'occurence associée, par ordre d'occurence décroissant **/
	public void AfficherTrier(){
		TreeSet<String> jetonsTries = new TreeSet<String>(new Comparateur(this));
		jetonsTries.addAll(this.keySet());
		Iterator<String> it = jetonsTries.descendingIterator();
		while (it.hasNext()) {
			String j = it.next();
			int occ = this.obtenirOccurence(j);
			try {
				System.out.printf(String.format("%-30s : %-10d",j,occ));
			}
			catch (Exception ex) {
				System.out.print(j+"    :     "+occ);
			}
			System.out.println();
		}
	}
	
	/** enregistre la table dans le fichier en paramètre **/ 
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
	
	public void Afficher() {
		Iterator<HashMap.Entry<String, Integer>> it = this.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<String, Integer> pair = it.next();
			String j = pair.getKey();
			int occ = pair.getValue();
			try {
				System.out.printf(String.format("%-30s : %-10d",j,occ));
			}
			catch (Exception ex) {
				System.out.print(j+"    :     "+occ);
			}
			System.out.println();
		}
	}
	
	/** classe permettant de comparer 2 jetons selon leur nombre d'occurences contenu dans la table en attribut **/
	class Comparateur implements Comparator<String> {
		
		private TableOccur table;
		
		public Comparateur(TableOccur uneTable){
			super();
			table = uneTable;
		}
		
		/** retourne 1 si j1 a plus d'occurence j2, 0 si j1 a autant d'occurence que j2, -1 sinon **/
		public int compare(String j1, String j2) {
			int occ1 = table.getOrDefault(j1, 0);
			int occ2 = table.getOrDefault(j2, 0);
			return (occ1>occ2 ? 1 : (occ1 == occ2 ? 0 : -1));
		}
	}
}
