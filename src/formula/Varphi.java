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
public class Varphi {
	private final String THETA_SEPARATOR = "&";
	private final String LOGIC_NOT = "¬";
	private List<Theta> thetas;
	
	public Varphi( String varphi ) {
		thetas = new ArrayList<Theta>();
		
		buildUpThetas( varphi );
	}
	
	/**
	 * * * buildUpPredicates
	 * Splits the Varphi in Thetas
	 * 
	 * @return void
	 */
	private void buildUpThetas( String varphi ) {
		String[] ps = varphi.split( THETA_SEPARATOR );
		
		//	splitting Varphis to create Thetas
		for( int i=0; i<ps.length; i++ )
			if( ps[i].startsWith(LOGIC_NOT) )
				thetas.add( new Theta(ps[i], false) );
			else
				thetas.add( new Theta(ps[i], true) );
	}
	
	/**
	 * * * getPredicates
	 * Returns the Thetas in Varphi
	 * 
	 * @return List<String>		predicates in Varphi
	 */
	public List<Theta> getThetas() {
		return this.thetas;
	}
}