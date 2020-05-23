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
	 * * * addElement
	 * Adds an element to the corrispondent hashtable filtered by its dimension
	 * 
	 * @return Vertex			root
	 */
	public void printHashTable() {
		System.out.println( "DEBUG HASH-TABLE" );
		Vertex v;
		for( int i = 0; i < hashTable.size(); i++ ) {
			System.out.println( "PRINTING LISTs with DIMENSION "+i );
			ArrayList<Vertex> subHashTable = hashTable.get(i);
			for( int j = 0; j < subHashTable.size(); j++ ) {
				v = subHashTable.get( j );
				System.out.println( "the vertex is "+v.getLabels() );
			}
		}
	}

}
