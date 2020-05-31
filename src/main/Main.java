/**
 * 
 */
package main;

import java.util.List;
import java.util.Scanner;
import dotgraph.DatOutput;
import dotgraph.DotGraphInput;
import dotgraph.DotGraphOutput;
import formula.Formula;
import graph.Graph;
import translators.DotGraphToGraphTranslator;

/**
 * @author stykky
 *
 */
public class Main {
	final private static String RUNTIME_ERROR = "\n =======>> ERROR on args <<======="
			+ "\n USAGE: ./main /path/to/dotFile -t <formula> pFormula || where -t ∈ { -r, -d }";
	final private static String TYPE_ERROR = "\n =======>> ERROR <<======="
			+ "\n type -t ∈ { -r, -d }";
	final private static String ADDING_VERTICES_MESSAGE = " >> Need to add %d vertices << \n"
			+ "Please write the .dat file path in order to add them:\n";
	final private static String REPAIR_TYPE = "-r";
	final private static String DAMAGE_DIVERGE_TYPE = "-d";

	/**
	 * MAIN
	 * usage: -dotPathGraph -type -formula -pFormula
	 * -dotPathGraph	String		the path of dot graph file
	 * -type			String		type could be repair (-r) or damage&diverge (-d)
	 * -formula			String		therapy formula in DNF mode
	 * -pFormula		double		probability of the therapy formula
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		checkArgs( args );
		
		//	vars from args
		String dotGraphPath = args[0];
		boolean isRepair = getType( args[1] );
		String formula = args[2];
		double pFormula = Double.parseDouble( args[3] );
		
		//	general objects
		Formula f = new Formula( formula );
		DotGraphInput dotGraphInput = new DotGraphInput( dotGraphPath );
		Graph graph = new DotGraphToGraphTranslator( dotGraphInput ).getGraph();

		//	general settings graph
		graph.setType( isRepair );
		graph.addEdgeOnLeaf();
		graph.breadthFirstSearch( graph.getRoot(), f, pFormula );

		//	print (or ask for missing vertices)
		print( graph );
	}
	
	/**
	 * * * checkArgs
	 * Checks the arguments length
	 * 
	 * @param String[] args		args from runtime execution 
	 * @return void
	 */
	private static void checkArgs( String[] args ) {
		if( args.length != 4 )
			throw new RuntimeException( RUNTIME_ERROR );
	}
	
	/**
	 * * * print
	 * Prints the output or ask for missing vertices
	 * 
	 * @return void
	 */
	@SuppressWarnings("resource")
	private static void print( Graph graph ) {
		//	print dotGraph
		if( graph.isValid() ) {
			DotGraphOutput dotGraphOutput = new DotGraphOutput( graph );
			dotGraphOutput.print();
		}
		//	need to add vertices into the file
		else {
			List<List<String>> missingVertices = graph.getMissingVertices();
			Scanner readLine = new Scanner(System.in);
			
			System.out.format( ADDING_VERTICES_MESSAGE, missingVertices.size() );
			String datFilePath = readLine.nextLine();
			
			DatOutput dotOutput = new DatOutput( datFilePath );
			dotOutput.writeVertices( missingVertices );
		}
	}
	
	/**
	 * * * getType
	 * Returns if the type is repair
	 * 
	 * @param String type		arg from runtime execution 
	 * @return true				validates the repair type
	 */
	private static boolean getType( String type ) {
		if( type.equals(REPAIR_TYPE) )
			return true;
		if( type.equals(DAMAGE_DIVERGE_TYPE) )
			return false;
		
		throw new RuntimeException( TYPE_ERROR );
	}

}
