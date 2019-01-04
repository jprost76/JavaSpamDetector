import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Program {
	
	/** chemin d'accès au fichier de la table d'occurence des Spam **/
	private final  String PATH_SPAM = "res/TableSpam";
	
	/** chemin d'accès au fichier de la table d'occurence des Ham **/
	private final  String PATH_HAM = "res/TableHam";
	
	/** chemin d'accès au rep de test **/
	private final static  String PATH_TEST = "src/Spam/1.txt";
	
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
	
	public Predicteur setPredicteur() {
		try {
			TableProba tproba = new TableProba(this.toccurSpam,this.toccurHam);
			predicteur = new Predicteur(tproba);
			return predicteur;
		} catch (TableNonInitException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Program prog = new Program();
		//chargement de la table d'occurence des Spam
		try {
			prog.loadTableSpam();
			//System.out.println("table Spam :");
			//prog.toccurSpam.AfficherTrier();
		}
		catch (FileNotFoundException exf) {
			System.out.println("fichier non trouv�");
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
			//System.out.println("table Ham :");
			//prog.toccurHam.AfficherTrier();
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
		/*File fic = new File(PATH_HAM);
		prog.extracteur.extraireCorpus(fic,prog.extracteur.toccurHam);
		File fic2 = new File(PATH_SPAM);
		prog.extracteur.extraireCorpus(fic2,prog.extracteur.toccurSpam);
		File f = new File("TableSpam");
		prog.extracteur.toccurSpam.save(f);
		File f2 = new File("TableHam");
		prog.extracteur.toccurHam.save(f2);*/
		Predicteur pred = prog.setPredicteur();
		//pred.getTProba().AfficherTrier(400);
		ArrayList<String> test = prog.extracteur.extraireMail(PATH_TEST);
		prog.predicteur.AfficherJetonsTriees(test);
		float res1 = pred.probaSpam(test);
		float res2 = pred.probaSpam2(test);
		System.out.println(res1+",  "+res2);
	}

}
