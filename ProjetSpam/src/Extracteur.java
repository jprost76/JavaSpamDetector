import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Extracteur {

	TableOccur toccurSpam;
	TableOccur toccurHam;
	
	Extracteur(){
		this.toccurSpam= new TableOccur();
		this.toccurHam= new TableOccur();
	}
	
	public void extraire(String pfichier,TableOccur toccur) {
	
	BufferedReader IN=null;
	try {
		IN = new BufferedReader (new FileReader(pfichier));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	String ligne;
	String [] mot;
	try {
		while ((ligne = IN.readLine ())!= null){
		     mot = ligne.split (" ");
		     for (int i = 0; i < mot.length; i++)
		     {
		    	 if (this.pretraitement(mot[i]))
		    	 {
		         	if (toccur.containsKey(mot[i])) 
		         		{
		         			int occur=toccur.get(mot[i]).intValue();
		         			toccur.put(mot[i],occur+1);
		         		}
		         	else
		         		{
		         		toccur.put(mot[i],1);
		         		}
		    	 }
		     }
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
toccur.setNbmail(toccur.getNbmail()+1);
}
	
	public void extraireCorpus(File repertoire,TableOccur toccur) {
	String [] listefichiers; 
	listefichiers=repertoire.list(); 
	System.out.println(listefichiers.length);
	for (int i=0;i<listefichiers.length;i++)
	{
		//String temp = repertoire.getName() +"/"  + listefichiers[i];
		String temp = repertoire.getAbsolutePath() +"/"  + listefichiers[i];
		this.extraire(temp,toccur);
	}
	}
	
	public ArrayList<String> extraireMail(String pmail) {
		TableOccur tableTemp = new TableOccur();
		this.extraire(pmail, tableTemp);
		ArrayList<String> motMail = new ArrayList<String>();
		Set<String> key= tableTemp.keySet();
		for (String s : key) {
			motMail.add(s);
		}
		return motMail;
	}
	
	public boolean pretraitement(String mot)
	{
		if (mot.length()>14 || mot.length()==0)
			return false;
		if (mot.charAt(0)== '<')
			return false;
		if (mot.charAt(mot.length()-1)== '>')
			return false;
		return true;
	}
	
}

