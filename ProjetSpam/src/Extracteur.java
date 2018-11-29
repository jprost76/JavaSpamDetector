import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Extracteur {

	TableOccur toccur;
	
	public void extraire(String pfichier) {
	
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
		         if (this.toccur.containsKey(mot[i])) 
		         	{
		        	int occur= this.toccur.get(mot[i]).intValue();
		        	this.toccur.put(mot[i],occur+1);
		         	}
		         else
		         	{
		        	 this.toccur.put(mot[i],1);
		         	}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
this.toccur.setNbmail(this.toccur.getNbmail()+1);
}
	
	public void extraireCorpus() {}
	
}