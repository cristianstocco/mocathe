/**
 * 
 */
package dotgraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author stykky
 *
 */
public class DotGraphInput {
	private final String FILE_NOT_FOUND_ERROR = "\n =======>> ERROR <<======="
			+ "\n dot file path not valid";
	private final String IO_EXCEPTION_ERROR = "\n =======>> ERROR <<======="
			+ "\n dot file path not readable";
	private File graphDescriptor;
	private FileReader graph;
	
	/**
	 * CONSTRUCTOR
	 */
	public DotGraphInput( String dotGraphPath ) {
		this.graphDescriptor = new File( dotGraphPath );
	}
	
	/**
	 * * * getContent
	 * Returns the content of the file
	 * 
	 * @return String			file content
	 */
	public String getContent() {
		if( !graphDescriptor.exists() )
			return null;

		//	init buffer
		long graphLength = graphDescriptor.length();
		char[] buffer = new char[ (int)graphLength ];
		
		try {
			//	catching the content
			this.graph = new FileReader( graphDescriptor );
			this.graph.read( buffer );
			
			return new String( buffer );
		} catch (FileNotFoundException e) {
			throw new RuntimeException( FILE_NOT_FOUND_ERROR );
		} catch (IOException e) {
			throw new RuntimeException( IO_EXCEPTION_ERROR );
		}
	}
}
