/**
 * 
 */
package formula;

import java.util.ArrayList;
import java.util.List;

import graph.Vertex;

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
	 * * * activates
	 * Returns if Varphi activates the Vertex v
	 * 
	 * @return boolean			if Varphi activates Vertex v
	 */
	public boolean activates(Vertex v) {
		for( int i=0; i<getThetas().size(); i++ )
			if( !v.getLabels().contains( getThetas().get(i).getTheta() ) )
				return false;
		
		return true;
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