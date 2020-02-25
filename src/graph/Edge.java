/**
 * 
 */
package graph;

/**
 * @author stykky
 *
 */
public class Edge {
	private Vertex v1;
	private Vertex v2;
	private double weight;
	
	/**
	 * CONSTRUCTOR
	 */
	public Edge( Vertex v1, Vertex v2, double weight ) {
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}
	
	/**
	 * * * getV1
	 * Returns the starting vertex
	 * 
	 * @return Vertex			starting vertex
	 */
	public Vertex getV1() {
		return this.v1;
	}

	/**
	 * * * getV2
	 * Returns the arriving vertex
	 * 
	 * @return Vertex			arriving vertex
	 */
	public Vertex getV2() {
		return this.v2;
	}

	/**
	 * * * getWright
	 * Returns the weight
	 * 
	 * @return double			weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * * * setWright
	 * Sets the weight
	 * 
	 * @return void
	 */
	public void setWeight( double weight ) {
		this.weight = weight;
	}
}
