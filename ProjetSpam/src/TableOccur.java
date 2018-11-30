import java.util.HashMap;

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
}
