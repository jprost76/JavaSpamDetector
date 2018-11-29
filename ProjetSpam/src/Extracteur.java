import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		    	 System.out.println(mot[i]);
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
	} catch (IOException e) {
		e.printStackTrace();
	}
toccur.setNbmail(toccur.getNbmail()+1);
}
	
	public void extraireCorpus(File repertoire,TableOccur toccur) {
	String [] listefichiers; 
	listefichiers=repertoire.list(); 
	for (int i=0;i<listefichiers.length;i++)
	{
		String temp = repertoire.getName() +"/"  + listefichiers[i];
		this.extraire(temp,toccur);
	}
	}
	
	
}

