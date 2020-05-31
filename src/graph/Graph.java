package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import HashTables.HashTable;
import formula.Formula;
import formula.Varphi;

public class Graph {
	private final String VERTEX_NOT_FOUND_INDEX = "\n =======>> ERROR <<======="
			+ "\n vertex not found by index";
	private final String ROOT_SAMPLES = "clonal";
	private final double LEAF_PROBABILITY = 1;
	private final HashTable hash;
	private Vertex root;
	private Vertex fVertex;
	private List<Vertex> vertices;
	private List<Edge> edges;
	private List<List<String>> missingVertices;
	private boolean isDirected;
	private boolean isRepair;
	private boolean isValid;
	
	/**
	 * CONSTRUCTOR
	 */
	public Graph( boolean isDirected ) {
		this.hash = new HashTable();
		this.root = null;
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		this.isDirected = isDirected;
		this.isValid = true;
		this.missingVertices = new ArrayList<List<String>>();
	}
	
	/**
	 * * * addEdgesAndBalanceFromActivation
	 * Sets the type of the graph
	 * 
	 * @return void
	 */
	public void breadthFirstSearch( Vertex source, Formula f, double pFormula ) {
		Vertex v = source;
		Vertex vToQueue;
		List<Edge> edges = v.getEdges();
		Queue<Vertex> queue = new LinkedList<Vertex>();
		
		//	set up queue
		queue.add( v );
		
		//	looping for queue vertices
		while( !queue.isEmpty() ) {
			v = queue.poll();
			
			//	checking activation for repair
			if( v.isActivated(f) && this.isRepair )
				addFreshRepairEdge( v, f, pFormula );
			//	checking activation for damage/diverge
			if( v.isActivated(f) && !this.isRepair )
				addFreshDamageDivergeEdge( v, pFormula );
			
			//	queueing
			edges = v.getEdges();
			for( int i = 0; i < edges.size(); i++ ) {
				vToQueue = edges.get(i).getV2();
				
				if( vToQueue.isQueueFresh() ) {
					queue.add( vToQueue );
					vToQueue.setQueueFresh( false );
				}
			}
		}
	}
	
	/**
	 * * * addFreshEdge
	 * Adds a fresh edge from activation
	 * 
	 * @return void
	 */
	private void addFreshRepairEdge( Vertex v, Formula f, double pFormula ) {
		balanceEdges( v, pFormula );
		
		List<String> filter = getActivationFilter( v, f );
		Vertex arrivalVertex = hash.findVertex( filter );
		
		addEdge( v, arrivalVertex, pFormula, true );
	}
	
	/**
	 * * * addFreshEdge
	 * Adds a fresh edge from activation
	 * 
	 * @return void
	 */
	private void addFreshDamageDivergeEdge( Vertex v, double pFormula ) {
		balanceEdges( v, pFormula );
		
		addEdge( v, getFVertex(), pFormula, true );
	}
	
	/**
	 * * * findVertex
	 * Returns the Vertex by the filter
	 * 
	 * @return Vertex			Vertex (or null)
	 */
	public Vertex findVertex( List<String> filter ) {
		for( int i=0; i<vertices.size(); i++ )
			if( vertices.get(i).getLabels().equals( filter ) )
				return vertices.get(i);
		
		//	queuing new vertex and invalidating the graph
		this.isValid = false;
		if( !this.missingVertices.contains(filter) )
			this.missingVertices.add( filter );
		
		return null;
	}
	
	/**
	 * * * addEdgeOnLeaf
	 * Adds edges on leafs
	 * 
	 * @return void
	 */
	public void addEdgeOnLeaf() {
		List<Vertex> vertices = getVertices();
		List<Vertex> leafs = new ArrayList<Vertex>();
		List<Edge> edges = getEdges();
		
		//	copying vertices
		for( int i=0; i<vertices.size(); i++ )
			leafs.add( vertices.get(i) );
		
		//	removing internal nodes
		for( int i=0; i<edges.size(); i++ )
			leafs.remove( edges.get(i).getV1() );
		
		//	adding self-loop to leafs
		for( int i=0; i<leafs.size(); i++ )
			addEdge( leafs.get(i), leafs.get(i), LEAF_PROBABILITY, false );
	}
	
	/**
	 * * * balanceEdges
	 * Balances the edges activated by Varphi
	 * 
	 * @return void
	 */
	private void balanceEdges( Vertex v, double pFormula ) {
		double multiplier = 1 - pFormula;
		Edge e;
		
		for( int i=0; i<v.getEdges().size(); i++ ) {
			e = v.getEdges().get( i );
			
			e.setWeight( e.getWeight() * multiplier );
		}
	}
	
	/**
	 * * * getActivationFilter
	 * Returns the filter for the arrival vertex activated
	 * 
	 * @return List<String>		filter
	 */
	private List<String> getActivationFilter( Vertex v, Formula f ) {
		List<String> labels = v.getLabels();
		List<String> filter = new ArrayList<String>();
		Varphi vp;
		
		//	copying labels
		for( int i=0; i<labels.size(); i++ )
			filter.add( labels.get(i) );
		
		//	creating the filter removing thetas
		for( int i=0; i<f.getVarphis().size(); i++ ) {
			vp = f.getVarphis().get( i );
			
			if( vp.activates(v) ) {
				for( int j=0; j<vp.getThetas().size(); j++ )
					filter.remove( vp.getThetas().get(j).getTheta() );
			}
		}
		
		return filter;
	}
	
	/**
	 * * * addVertex
	 * Adds a vertex to the graph
	 * 
	 * @return void
	 */
	public void addVertex( int index, String labels, double probability, String samples ) {
		Vertex v = new Vertex(index, labels, probability, samples);
		int labelsNumber = 0;
		
		//	root vertex (clonal)
		if( samples.toLowerCase().equals( ROOT_SAMPLES ) )
			this.root = v;
		else
			labelsNumber = labels.split(",").length;
		
		hash.addElement( v, labelsNumber );
		vertices.add( v );
	}
	
	/**
	 * * * addVertex
	 * Adds an edge to the graph
	 * 
	 * @return void
	 */
	public void addEdge( Vertex v1, Vertex v2, double weight, boolean isAdded ) {
		Edge e = new Edge(v1, v2, weight, isAdded);
		edges.add( e );
		v1.addEdge( e );
	}
	
	/**
	 * * * getRoot
	 * Returns the root vertex
	 * 
	 * @return Vertex			root
	 */
	public Vertex getRoot() {
		return this.root;
	}
	
	/**
	 * * * findVertex
	 * Returns the Vertex by the index
	 * 
	 * @return Vertex			Vertex (or null)
	 */
	public Vertex getVertex( int index ) {
		try {
			return vertices.get(index);
		}
		catch ( IndexOutOfBoundsException error ) {
			throw new RuntimeException( VERTEX_NOT_FOUND_INDEX );
		}
	}
	
	/**
	 * * * getVertices
	 * Returns the graph vertices
	 * 
	 * @return List<Vertices>	graph vertices
	 */
	public List<Vertex> getVertices() {
		return this.vertices;
	}
	
	/**
	 * * * setType
	 * Sets the type of the graph
	 * 
	 * @return List<Vertices>	graph vertices
	 */
	public void setType( boolean isRepair ) {
		this.isRepair = isRepair;
		
		if( !this.isRepair ) {
			this.fVertex = new FVertex(vertices.size());
			vertices.add( this.fVertex );
		}
	}
	
	/**
	 * * * getEdges
	 * Returns the graph edges
	 * 
	 * @return List<Edge>		graph edges
	 */
	public List<Edge> getEdges() {
		return this.edges;
	}
	
	/**
	 * * * getFVertex
	 * Returns the F vertex
	 * 
	 * @return Vertex			FVertex
	 */
	public Vertex getFVertex() {
		return this.fVertex;
	}
	
	/**
	 * * * getMissingVertices
	 * Returns the missing vertices
	 * 
	 * @return List<List<String>>		missing vertices
	 */
	public List<List<String>> getMissingVertices() {
		return this.missingVertices;
	}
	
	/**
	 * * * isDirected
	 * Returns if the graph is directed
	 * 
	 * @return boolean			directed attribute
	 */
	public boolean isDirected() {
		return this.isDirected;
	}
	
	/**
	 * * * isValid
	 * Returns if the graph is valid (missing some vertex)
	 * 
	 * @return boolean			valid attribute
	 */
	public boolean isValid() {
		return this.isValid;
	}
}
