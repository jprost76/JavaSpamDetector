import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Program {
	
	/** chemin d'accès au fichier de la table d'occurence des Spam **/
	private final  String PATH_SPAM = "res/TableSpam";
	
	private final  static String CORPUS_SPAM = "corpus/Spam";
	/** chemin d'accès au fichier de la table d'occurence des Ham **/
	private final  String PATH_HAM = "res/TableHam";
	
	private final  static String CORPUS_HAM = "corpus/Ham";
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
	
	public Predicteur getPredicteur() {
		return predicteur;
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
			System.out.println("table Ham :");
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
		
		Predicteur pred = prog.setPredicteur();
		//affichage de la table de probabilité
		//prog.getPredicteur().getTProba().AfficherTrier(400);
		File repertoire = new File("src/Spam");
		String [] listefichiers; 
		listefichiers=repertoire.list(); 
		System.out.println(listefichiers.length);
		for (int i=0;i<listefichiers.length;i++)
		{
			String temp = repertoire.getAbsolutePath() +"/"  + listefichiers[i];
			ArrayList<String> test = prog.extracteur.extraireMail(temp);
			prog.getPredicteur().AfficherJetonsTriees(test);
			float res = pred.probaSpam(test);
			System.out.println("probabilité que le mail soit un spam : "+res);
		}
	}

}
