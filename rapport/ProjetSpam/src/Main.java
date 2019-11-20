import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		File rep=null;
		Scanner sc = new Scanner(System.in);
		boolean ficok=false;
		while (!ficok) {
			System.out.println("Veuillez choisir votre repertoire");
			String str = sc.nextLine();
			rep = new File(str);
			if (rep.exists())
				ficok=true;
		}
		System.out.println("Est ce un corpus de Spam(1) ou de Ham(2)");
		Extracteur ex = new Extracteur();
		int chx =sc.nextInt();
		if (chx==1) 
		{
			ex.extraireCorpus(rep,ex.toccurSpam);
		}
		else
		{
			ex.extraireCorpus(rep,ex.toccurHam);
		}
		sc.close();
		System.out.println("Corpus Extrait !");
		if (chx==1) {
			System.out.println("Il y avait : " + ex.toccurSpam.getNbmail() +"mail");
			ex.toccurSpam.AfficherTrier();
		} else {
			System.out.println("Il y avait : " + ex.toccurHam.getNbmail() +"mail");
			ex.toccurHam.AfficherTrier();
		}
		
	}
}
