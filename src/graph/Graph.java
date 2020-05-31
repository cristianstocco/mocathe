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
	private final String F_VERTEX_NOT_FOUND = "\n =======>> ERROR <<======="
			+ "\n F vertex not found";
	private final String ROOT_SAMPLES = "clonal";
	private final double LEAF_PROBABILITY = 1;
	private final HashTable hash;
	private Vertex root;
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
	public void breadthFirstSearch( Vertex source, Formula f ) {
		Vertex v = source;
		Vertex vToQueue;
		List<Edge> edges = v.getEdges();
		Queue<Vertex> queue = new LinkedList<Vertex>();
		
		queue.add( v );
		
		while( !queue.isEmpty() ) {
			v = queue.poll();
			if( v.isActivated(f) ) {
				System.out.println( " >>>>>>>>>>>>>> vertex "+v.getLabels()+" IS ACTIVATED" );
			}
			edges = v.getEdges();
			for( int i = 0; i < edges.size(); i++ ) {
				vToQueue = edges.get(i).getV2();
				if( vToQueue.getQueueFresh() ) {
					queue.add( vToQueue );
					vToQueue.setQueueFresh( false );
				}
			}
		}
	}
	
	/**
	 * * * addEdgesAndBalanceFromActivation
	 * Sets the type of the graph
	 * 
	 * @return void
	 */
	public void addEdgesAndBalanceFromActivation( Formula f, double pFormula ) {
		List<Vertex> vertices = getVertices();
		Vertex v;
		Vertex arrivalVertex;
		
		//	checking which vertices are being activated
		for(int i = 0; i < vertices.size(); i++) {
			v = vertices.get(i);
			
			if( v.isActivated( f ) ) {
				balanceEdges( v, pFormula );
				
				//	whether the mocathe is Repair type
				if( this.isRepair ) {
					arrivalVertex = arrivalVertex(v, f);
					
					if( this.isValid )
						addEdge( v, arrivalVertex, pFormula, true );
				}
				//	whether the mocathe is Damage or Diverge type
				else
					addEdge( v, getFVertex(), pFormula, true );
			}
		}
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
		List<Edge> edges = getEdges();
		double multiplier = 1 - pFormula;
		Edge e;
		
		//	balancing all activated edges
		for( int i=0; i<edges.size(); i++ ) {
			e = edges.get( i );
			
			if( e.getV1().equals(v) )
				e.setWeight( e.getWeight() * multiplier );
		}
	}
	
	/**
	 * * * arrivalVertex
	 * Returns the arrival vertex activated and filtered by formula
	 * 
	 * @return List<Edge>		graph edges
	 */
	private Vertex arrivalVertex( Vertex v, Formula f ) {
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
		
		return findVertex( filter );
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
	 * @return Edge				added edge
	 */
	public Edge addEdge( Vertex v1, Vertex v2, double weight, boolean isAdded ) {
		Edge e = new Edge(v1, v2, weight, isAdded);
		edges.add( e );
		return e;
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
		
		if( !this.isRepair )
			vertices.add( new FVertex(vertices.size()) );
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
		Vertex v;
		
		//	finding FVertex
		for( int i=0; i<vertices.size(); i++ ) {
			v = vertices.get(i);
			if( v instanceof FVertex )
				return v;
		}
			
		throw new RuntimeException( F_VERTEX_NOT_FOUND );
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
	
	/**
	 * * * hashTableDebug
	 * Debugs the hash table
	 * 
	 * @return void
	 */
	public void hashTableDebug() {
		hash.printHashTable();
	}
}
