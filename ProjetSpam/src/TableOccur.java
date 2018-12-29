import java.util.HashMap;
import java.util.Iterator;



@SuppressWarnings("serial")
public class TableOccur extends HashMap<String,Integer> {
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
}
