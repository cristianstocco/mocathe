/**
 * 
 */
package HashTables;

import java.util.ArrayList;
import java.util.List;
import graph.Vertex;

/**
 * @author stykky
 *
 */
public class HashTable {
	final private static String VERTEX_NOT_FOUND = "\n =======>> ERROR <<======="
					+ "vertex not found in hash-table, not cached";
	private List<ArrayList> hashTable;
	
	/**
	 * CONSTRUCTOR
	 */
	public HashTable() {
		hashTable = new ArrayList<ArrayList>();
	}
	
	/**
	 * * * addElement
	 * Adds an element to the corrispondent hashtable filtered by its dimension
	 * 
	 * @return Vertex			root
	 */
	@SuppressWarnings("unchecked")
	public void addElement( Vertex v, int index ) {
		addSubHashTable( index );
		ArrayList<Vertex> subTable = hashTable.get( index );
		subTable.add( v );
	}
	
	/**
	 * * * addSubHashTable
	 * Adds the sub hash table when it is not present
	 * 
	 * @return void
	 */
	public void addSubHashTable( int index ) {
		try {
			hashTable.get( index );
		}
		catch( IndexOutOfBoundsException error ) {
			for( int i = hashTable.size() ; i <= index; i++ )
				hashTable.add( i, new ArrayList<Vertex>() );
		}
	}
	
	/**
	 * * * findVertex
	 * Returns vertex by the filter looking for into the hashtables
	 * 
	 * @return Vertex		arrival vertex
	 */
	@SuppressWarnings("unchecked")
	public Vertex findVertex( List<String> filter ) {
		ArrayList<Vertex> vertices = hashTable.get( filter.size() );
		
		Vertex v;
		for( int i=0; i < vertices.size(); i++ ) {
			v = vertices.get( i );
			
			if( v.getLabels().equals( filter ) )
				return v;
		}
		
		throw new RuntimeException( VERTEX_NOT_FOUND );
	}

}
