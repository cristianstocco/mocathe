/**
 * 
 */
package formula;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stykky
 *
 */
public class Formula {
	private final String VARPHI_SEPARATOR = "\\|";
	private String formula;
	private List<Varphi> varphis;
	
	public Formula( String formula ) {
		this.formula = formula;
		this.varphis = new ArrayList<Varphi>();
		
		buildUpVarphis();
	}
	
	/**
	 * * * buildUpVarphis
	 * Splits the Formula in Varphis
	 * 
	 * @return void
	 */
	private void buildUpVarphis() {
		String[] subformulas = this.formula.trim().replaceAll(" ", "").split( VARPHI_SEPARATOR );
		
		//	splitting formula to create Varphis
		for( int i=0; i<subformulas.length; i++ )
			varphis.add( new Varphi(subformulas[i]) );
	}
	
	/**
	 * * * getVarphis
	 * Returns the Varphis attribute
	 * 
	 * @return List<Varphi>		varphis
	 */
	public List<Varphi> getVarphis() {
		return this.varphis;
	}
}
