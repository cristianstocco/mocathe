/**
 * 
 */
package graph;

import java.util.ArrayList;

/**
 * @author stykky
 *
 */
public class FVertex extends Vertex {
	private final String F_VERTEX_LABEL = "F";
	private final double F_VERTEX_PROBABILITY = 0.0;
	private final String F_VERTEX_SAMPLES = "XXX";

	/**
	 * CONSTRUCTOR
	 */
	public FVertex( int index ) {
		this.index = index;
		this.labels = new ArrayList<String>();
		this.probability = F_VERTEX_PROBABILITY;
		this.samples = F_VERTEX_SAMPLES;
		
		super.buildUpLabels( F_VERTEX_LABEL );
	}

}
