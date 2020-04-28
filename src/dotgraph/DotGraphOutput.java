package dotgraph;

import java.util.List;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class DotGraphOutput {
	private final String DIRECTED_GRAPH_HEADER = "digraph {";
	private final String NOT_DIRECTED_GRAPH_HEADER = "graph {";
	private final String GRAPH_FOOTER = "}";
	private final String VERTEX_FORMAT = "%d [label=\"%s, %.3f\nSamples: [%s]\n\"]\n";
	private final String EDGE_FORMAT = "%d -> %d[label=\"%.3f\"]\n";
	private final String ADDED_EDGE_FORMAT = "%d -> %d[label=\"%.3f\", color=blue]\n";
	private Graph graph;
	
	/**
	 * CONSTRUCTOR
	 */
	public DotGraphOutput( Graph graph ) {
		this.graph = graph;
	}
	
	/**
	 * * * print
	 * Prints the dot graph
	 *  
	 * @return void
	 */
	public void print() {
		if( graph.isDirected() )
			System.out.println( DIRECTED_GRAPH_HEADER );
		else
			System.out.println( NOT_DIRECTED_GRAPH_HEADER );
		
		printVertices();
		printEdges();
		
		System.out.println( GRAPH_FOOTER );
	}

	/**
	 * * * printVertices
	 * Prints the dot graph vertices
	 *  
	 * @return void
	 */
	private void printVertices() {
		List<Vertex> vertices = graph.getVertices();
		Vertex v;
		
		//	printing all vertices
		for( int i=0; i<vertices.size(); i++ ) {
			v = vertices.get( i );
			
			System.out.printf(VERTEX_FORMAT, v.getIndex(), v.getLabels(), v.getProbability(), v.getSamples());
		}
	}

	/**
	 * * * printEdges
	 * Prints the dot graph edges
	 *  
	 * @return void
	 */
	private void printEdges() {
		List<Edge> edges = graph.getEdges();
		Edge e;
		
		//	printing all edges
		for( int i=0; i<edges.size(); i++ ) {
			e = edges.get( i );
			
			if( e.getIsAdded() )
				System.out.printf(ADDED_EDGE_FORMAT, e.getV1().getIndex(), e.getV2().getIndex(), e.getWeight());
			else
				System.out.printf(EDGE_FORMAT, e.getV1().getIndex(), e.getV2().getIndex(), e.getWeight());
		}
	}
}