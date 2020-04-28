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
	 * * * positiveLiterals
	 * Returns the Positive Literals attribute
	 * 
	 * @return List<Varphi>		positive literals
	 */
	public List<Theta> positiveLiterals() {
		List<Theta> positiveLiterals = new ArrayList<Theta>();
		List<Theta> thetas;
		
		//	cycling over Varphis
		for( int i=0; i<varphis.size(); i++ ) {
			thetas = varphis.get(i).getThetas();
			
			//	cycling over Thetas to take positive Thetas
			for( int j=0; j<thetas.size(); j++ )
				if( thetas.get(j).isPositive() && !positiveLiterals.contains(thetas.get(j)) )
					positiveLiterals.add( thetas.get(j) );
		}
		
		return positiveLiterals;
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
