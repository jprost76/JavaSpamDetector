import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class TestCorpus {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Program prog = new Program();
		//chargement de la table d'occurence des Spam
		try {
			prog.loadTableSpam();
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
		//File repertoire = new File("src/Spam");
		//System.out.println(prog.toccurHam.nbMail);
		//System.out.println(prog.toccurSpam.nbMail);
		File repertoire = new File("src/Spam/verif");
		String [] listefichiers; 
		listefichiers=repertoire.list(); 
		System.out.println(listefichiers.length);
		int nbHamDetected =0;
		int nbSpamDetected =0;
		int doubt =0;
		for (int i=0;i<listefichiers.length;i++)
		{
			String temp = repertoire.getAbsolutePath() +"/"  + listefichiers[i];
			ArrayList<String> test = prog.getExtracteur().extraireMail(temp);
			prog.getPredicteur().AfficherJetonsTriees(test);
			float res = pred.probaSpam(test);
			if (res <0.3) {
				nbHamDetected++;
			}
			else 
			{ System.out.println(temp);
			System.out.println(res);
				if (res >0.7) {
					nbSpamDetected++;
					
				}
				else
					doubt++;
			}
		}
		System.out.println("Nb de Ham :" + nbHamDetected);
		System.out.println("Nb de Spam :" + nbSpamDetected);
		System.out.println("Nb de Doutes :" + doubt);
	}

}
