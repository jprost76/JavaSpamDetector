/**
 * Exception levée si une table d'occurence non initialisée est utilisée
 * @author jprost
 *
 */
public class TableNonInitException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableNonInitException() {
		super("les table d'occurence ne sont pas intialisées ");
	}

}
