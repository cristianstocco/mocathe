/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.List;

import formula.Formula;
import formula.Theta;
import formula.Varphi;

/**
 * @author stykky
 *
 */
public class Vertex {
	private final String LABEL_SEPARATOR = ", ";
	protected int index;
	protected List<String> labels;
	protected double probability;
	protected String samples;
	private List<Edge> edges;
	
	/**
	 * CONSTRUCTOR
	 */
	public Vertex() {}
	public Vertex( int index, String labels, double probability, String samples ) {
		this.index = index;
		this.labels = new ArrayList<String>();
		this.probability = probability;
		this.samples = samples;
		this.edges = new ArrayList<Edge>();
		
		buildUpLabels( labels );
	}
	
	/**
	 * * * isActivated
	 * Returns if the vertex is activated
	 * 
	 * @return boolean			index attribute
	 */
	public boolean isActivated( Formula f ) {
		List<Varphi> varphis = f.getVarphis();
		List<Theta> thetas;
		boolean isValid;
		
		//	"clonal vertex" & "F vertex" can not be activated
		if( this.labels.size() == 0 || this instanceof FVertex )
			return false;
		
		//	finding any Varphi that can activate the vertex
		for( int i=0; i<varphis.size(); i++ ) {
			thetas = varphis.get(i).getThetas();
			isValid = true;
			
			//	validating all thetas in Varphi
			for( int j=0; j<thetas.size() && isValid; j++) {
				if( thetas.get(j).isPositive() && !this.labels.contains(thetas.get(j).getTheta()) )
					isValid = false;
				
				if( !thetas.get(j).isPositive() && this.labels.contains(thetas.get(j).getTheta()) )
					isValid = false;
			}
			
			if( isValid )
				return true;
		}
		
		return false;
	}
	
	/**
	 * * * buildUpLabels
	 * Builds up labels
	 * 
	 * @return void
	 */
	protected void buildUpLabels( String labels ) {
		if( labels.length() == 0 )
			return;
		
		//	splitting labels
		String[] labelTokens = labels.split( LABEL_SEPARATOR );
		
		for( int i=0; i<labelTokens.length; i++ )
			this.labels.add( labelTokens[ i ] );
	}
	
	/**
	 * * * linkEdge
	 * Links an edge to the current edges list
	 * 
	 * @return void
	 */
	public void addEdge( Edge e ) {
		edges.add( e );
	}
	
	/**
	 * * * getIndex
	 * Returns the index attribute of the vertex
	 * 
	 * @return int				index attribute
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * * * getLabels
	 * Returns the labels attribute of the vertex
	 * 
	 * @return String			labels attribute
	 */
	public List<String> getLabels() {
		return this.labels;
	}
	
	/**
	 * * * getProbability
	 * Returns the probability attribute of the vertex
	 * 
	 * @return double			probability attribute
	 */
	public double getProbability() {
		return this.probability;
	}
	
	/**
	 * * * getSamples
	 * Returns the samples attribute of the vertex
	 * 
	 * @return String			samples attribute
	 */
	public String getSamples() {
		return this.samples;
	}
	
	/**
	 * * * getEdges
	 * Returns the edges linked to the current vertex
	 * 
	 * @return List<Edge>		linked edges
	 */
	public List<Edge> getEdges() {
		return this.edges;
	}
	
	/**
	 * * * toString
	 * toString override
	 * 
	 * @return List<Edge>		linked edges
	 */
	public String toString() {
		return "__"+getLabels().toString()+"__";
	}
}
