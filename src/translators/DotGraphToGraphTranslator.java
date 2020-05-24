/**
 * 
 */
package translators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dotgraph.DotGraphInput;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * @author stykky
 *
 */
public class DotGraphToGraphTranslator {
	final String VERTEX_MATCH_ERROR = "\n =======>> ERROR <<======="
			+ "\n vertex matching error";
	final String EDGE_MATCH_ERROR = "\n =======>> ERROR <<======="
			+ "\n edge matching error";
	final String ROOT_NOT_FOUND = "\n =======>> ERROR <<======="
			+ "\n root not found";
	final String DIRECTED_GRAPH_PATTERN = "digraph";
	final String CLONAL_SAMPLES = "clonal";
	final String VERTEX_PATTERN = "\\d* \\[label=\\\"\\[[a-zA-Z, ]*\\], (\\d\\.\\d*)\\nSamples: \\[[\\w, ]*\\]\\n\\\"\\]";
	final String EDGE_PATTERN = "\\d* -> \\d*\\[label=\\\"(\\d\\.\\d*)\\\"\\]";
	final String FIRST_NUMBER_PATTERN = "^\\d*";
	final String LABELS_VERTEX_PATTERN = "\\\"\\[[a-zA-Z, ]*\\]";
	final String PROBABILITY_VERTEX_PATTERN = "([01]\\.\\d*)";
	final String SAMPLES_VERTEX_PATTERN = "[^\\\"]\\[[a-zA-Z0-9, ]*\\]";
	final String ARRIVING_VERTEX_PATTERN = "\\d*\\[";
	final String WEIGHT_PATTERN = "\\\"[\\.0-9]*\\\"";
	private String dotGraphContent;
	private Graph graph;
	
	/**
	 * CONSTRUCTOR
	 */
	public DotGraphToGraphTranslator( DotGraphInput dotGraphInput ) {
		dotGraphContent = dotGraphInput.getContent();
		graph = new Graph( isDirected() );
		
		buildGraph();
	}
	
	/**
	 * * * buildGraph
	 * Builds up the graph with vertices and edges
	 * 
	 * @return void
	 */
	private void buildGraph() {
		Pattern vertexPattern = Pattern.compile( VERTEX_PATTERN );
		Matcher vertexMatcher = vertexPattern.matcher( dotGraphContent );
		Pattern edgePattern = Pattern.compile( EDGE_PATTERN );
		Matcher edgeMatcher = edgePattern.matcher( dotGraphContent );

		//	building vertices
	    while( vertexMatcher.find() )
	    	createVertex( dotGraphContent.substring(vertexMatcher.start(), vertexMatcher.end()) );

	    //	building edges
	    while( edgeMatcher.find() )
	    	createEdge( dotGraphContent.substring(edgeMatcher.start(), edgeMatcher.end()) );
	    
	    //graph.hashTableDebug();
	    
	    if( graph.getRoot() == null )
	    	throw new RuntimeException( ROOT_NOT_FOUND );
	}
	
	/**
	 * * * createVertex
	 * Creates the vertex by the regex format
	 * 
	 * @return void
	 */
	private void createVertex( String vertex ) {
		Pattern indexPattern = Pattern.compile( FIRST_NUMBER_PATTERN );
		Matcher indexMatcher = indexPattern.matcher( vertex );
		Pattern labelsPattern = Pattern.compile( LABELS_VERTEX_PATTERN );
		Matcher labelMatcher = labelsPattern.matcher( vertex );
		Pattern probabilityPattern = Pattern.compile( PROBABILITY_VERTEX_PATTERN );
		Matcher probabilityMatcher = probabilityPattern.matcher( vertex );
		Pattern samplesPattern = Pattern.compile( SAMPLES_VERTEX_PATTERN );
		Matcher samplesMatcher = samplesPattern.matcher( vertex );

		//	vertices parameters
		int index;
		String labels;
		double probability;
		String samples;
		
		//	building vertex
	    if( indexMatcher.find() && labelMatcher.find() && probabilityMatcher.find() && samplesMatcher.find() ) {
	    	index = Integer.parseInt( vertex.substring(indexMatcher.start(), indexMatcher.end()) );
	    	labels = vertex.substring(labelMatcher.start()+2, labelMatcher.end()-1);
	    	probability = Double.parseDouble( vertex.substring(probabilityMatcher.start(), probabilityMatcher.end()) );
	    	samples = vertex.substring(samplesMatcher.start()+2, samplesMatcher.end()-1);
	    	if( samples.length() == 0 )
	    		samples = this.CLONAL_SAMPLES;
	    	
	    	graph.addVertex(index, labels, probability, samples);
	    }
	    //	vertex format not compatible
	    else
			throw new RuntimeException( VERTEX_MATCH_ERROR );
	}
	
	/**
	 * * * createEdge
	 * Creates the edge by the regex format
	 * 
	 * @return void
	 */
	private void createEdge( String edge ) {
		Pattern startingVertexIndexPattern = Pattern.compile( FIRST_NUMBER_PATTERN );
		Matcher startingVertexIndexMatcher = startingVertexIndexPattern.matcher( edge );
		Pattern arrivingVertexIndexPattern = Pattern.compile( ARRIVING_VERTEX_PATTERN );
		Matcher arrivingVertexIndexMatcher = arrivingVertexIndexPattern.matcher( edge );
		Pattern weightPattern = Pattern.compile( WEIGHT_PATTERN );
		Matcher weightMatcher = weightPattern.matcher( edge );

		//	edge parameters
		int startingVertexIndex;
		int arrivingVertexIndex;
		double weight;
		
		//	building edge
	    if( startingVertexIndexMatcher.find() && arrivingVertexIndexMatcher.find() && weightMatcher.find() ) {
	    	startingVertexIndex = Integer.parseInt( edge.substring(startingVertexIndexMatcher.start(), startingVertexIndexMatcher.end()) );
	    	arrivingVertexIndex = Integer.parseInt( edge.substring(arrivingVertexIndexMatcher.start(), arrivingVertexIndexMatcher.end()-1) );
	    	weight = Double.parseDouble( edge.substring(weightMatcher.start()+1, weightMatcher.end()-1) );
	    	
	    	Vertex v1 = graph.getVertex(startingVertexIndex);
	    	Vertex v2 = graph.getVertex(arrivingVertexIndex);
	    	Edge e = graph.addEdge(v1, v2, weight, false);
	    	v1.addEdge( e );
	    }
	    //	edge format not compatible
	    else
			throw new RuntimeException( EDGE_MATCH_ERROR );
	}
	
	/**
	 * * * isDirected
	 * Returns if the dot file is directed
	 * 
	 * @return boolean			directed value
	 */
	private boolean isDirected() {
		Pattern directedGraphPattern = Pattern.compile( DIRECTED_GRAPH_PATTERN );
		Matcher directedGraphMatcher = directedGraphPattern.matcher( dotGraphContent );

		return directedGraphMatcher.find();
	}
	
	/**
	 * * * getGraph
	 * Returns the graph
	 * 
	 * @return Graph			the graph	
	 */
	public Graph getGraph() {
		return graph;
	}
}
