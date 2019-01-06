import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;


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
	
	/**retourne la table de probabilité de l'attribut prédicteur,
	 *  ou null si le prédicteur n'est pas initialisé**/
	public TableProba getTProba(){
		if (predicteur != null) {
			return predicteur.getTProba();
		} else
			return null; 
	}

	public TableOccur getTableHam() {
		return toccurHam;
	}
	
	public TableOccur getTableSpam() {
		return toccurSpam;
	}
	
	public Extracteur getExtracteur() {
		return extracteur;
	}
	
	public static void main(String[] args) {
		Program prog = new Program();
		//chargement de la table d'occurence des Spam
		try {
			prog.loadTableSpam();
			System.out.println("table Spam chargée");
		}
		catch (FileNotFoundException exf) {
			System.out.println("fichier "+prog.PATH_SPAM+" non trouv�");
		}
		catch (ClassNotFoundException exc) {
			System.out.println("fichier "+prog.PATH_SPAM+" non valide");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		// chargement de la table d'occurence des Ham
		try {
			prog.loadTableHam();
			System.out.println("table Ham chargée");
		}
		catch (FileNotFoundException exf) {
			System.out.println("fichier "+prog.PATH_HAM+" non trouvé");
		}
		catch (ClassNotFoundException exc) {
			System.out.println("fichier" +prog.PATH_HAM+" non valide");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		prog.setPredicteur();
		
		Scanner sc = new Scanner(System.in);
		int choix = 0;
		while (choix !=5) {
			System.out.println("Que voulez vous faire :");
			System.out.println("1 : afficher la table d'occurence des spam");
			System.out.println("2 : afficher la table d'occurence des ham");
			System.out.println("3 : afficher la table de probabilité");
			System.out.println("4 : Déterminer la probabilité d'un mail");
			System.out.println("5 : Fin");
			while ((choix < 1) || (choix > 4)) {
				choix = sc.nextInt();
			}
			switch (choix) {
			case 1 : {
				prog.getTableSpam().AfficherTrier();
				System.out.printf(String.format("\nnombre de mots dans la table : %d\n\n",prog.getTableSpam().size()));
				choix = 0;
			}
				break;
			case 2 : {
				prog.getTableHam().AfficherTrier();
				System.out.printf(String.format("\nnombre de mots dans la table : %d\n\n",prog.getTableHam().size()));
				choix = 0;
			}
				break;
			case 3 : {
				int n = prog.getTProba().size();
				System.out.printf(String.format("il y a %d dans la table, combien souhaitez-vous en afficher?",n));
				int naff = sc.nextInt(); 
				prog.getTProba().AfficherTrier(naff);
				System.out.printf(String.format("\n \n"));
				choix = 0;
			}
				break;
			case 4 : {
				String nomFic = null;
				// un fichier qui n'existe pas!
				File ftemp = new File("ùéùéùéùéù  ààà ùéù  à  àà àà");
				while(!ftemp.exists()) {
					System.out.println("\nIndique le chemin du fichier à traiter\n");
					nomFic = sc.nextLine();
					ftemp = new File(nomFic);
				}
				ArrayList<String> mail = prog.getExtracteur().extraireMail(nomFic);
				System.out.println("\n jetons présent dans le mail et leur probabilités : \n");
				prog.getPredicteur().AfficherJetonsTriees(mail);
				float prob = prog.getPredicteur().probaSpam(mail);
				System.out.println("\nla probabilité que ce mail soit un spam est de : "+prob+"\n");
				choix = 0;
			} break;
			default : ;
			}
		}
		return;
	}
	
		

}
