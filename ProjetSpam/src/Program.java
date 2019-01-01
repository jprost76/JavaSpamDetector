import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Program {
	
	/** chemin d'accès au fichier de la table d'occurence des Spam **/
	private final String PATH_SPAM = "res/TableSpam";
	
	/** chemin d'accès au fichier de la table d'occurence des Ham **/
	private final String PATH_HAM = "res/TableHam";
	
	private TableOccur toccurSpam;
	
	private TableOccur toccurHam;
	
	private Extracteur  extracteur;
	
	private Predicteur predicteur;
	
	public Program() {
		extracteur = new Extracteur();
	}
	
	/** remplit la table d'occurence des Spam à partir d'un repértoire contenant un corpus de Spam **/
	public void remplirTableSpam(File fic) {
		extracteur.extraireCorpus(fic, this.toccurSpam);
	}
	
	/** remplit la table d'occurence des ham à partir d'un repértoire contenant un corpus de ham **/
	public void remplirTableHam(File fic) {
		extracteur.extraireCorpus(fic, this.toccurHam);
	}
	
	
	
	/** charge la table d'occurence pour les spam stockée dans le dossier res/ **/
	public void loadTableSpam() throws IOException, ClassNotFoundException {
		File fic = new File(PATH_SPAM);
		FileInputStream flux = new FileInputStream(fic);
		ObjectInputStream reader = new ObjectInputStream(flux);
		toccurSpam = (TableOccur) reader.readObject();
		reader.close();
		flux.close();
	}
	
	/** charge la table d'occurence pour les ham stockée dans le dossier res/ **/
	public void loadTableHam() throws IOException, ClassNotFoundException {
		File fic = new File(PATH_HAM);
		FileInputStream flux = new FileInputStream(fic);
		ObjectInputStream reader = new ObjectInputStream(flux);
		toccurHam = (TableOccur) reader.readObject();
		reader.close();
		flux.close();
	}
	
	public void setPredicteur() {
		try {
			TableProba tproba = new TableProba(toccurSpam,toccurHam);
			predicteur = new Predicteur(tproba);
		} catch (TableNonInitException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Program prog = new Program();
		//chargement de la table d'occurence des Spam
		try {
			prog.loadTableSpam();
			prog.toccurSpam.AfficherTrier();
		}
		catch (FileNotFoundException exf) {
			System.out.println("fichier non trouvé");
		}
		catch (ClassNotFoundException exc) {
			System.out.println("fichier non valide");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		// chargement de la table d'occurence des Ham
		try {
			prog.loadTableHam();
			prog.toccurHam.AfficherTrier();
		}
		catch (FileNotFoundException exf) {
			System.out.println("fichier non trouvé");
		}
		catch (ClassNotFoundException exc) {
			System.out.println("fichier non valide");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		// création du prédicteur*/
		prog.setPredicteur();
		System.out.println("Predicteur crée");
		
	}

}
