/**
 * 
 */
package dotgraph;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author stykky
 *
 */
public class DatOutput {
	private final String IO_EXCEPTION_ERROR = "\n =======>> ERROR <<======="
			+ "\n dot file path not readable";
	private final String HEADER_MATCH_ERROR = "\n =======>> ERROR <<======="
			+ "\n header format not matched";
	private final String GENES_MATCH_ERROR = "\n =======>> ERROR <<======="
			+ "\n genes sequence in the header format not matched";
	private final String NEW_VERTEX_SAMPLES = "XXX";
	private final String NEW_VERTEX_GENE = "\t%d";
	private final String HEADER_PATTERN = "S\\\\G.*";
	private final String HEADER_PATTERN_GENES = "[^S\\\\G\\s].*";
	private final String GENE_SEPARATOR = "[\\t ]*";
	private final String NEW_LINE = "\n";
	private File datDescriptor;
	private String genesSequence;
	private FileReader datInput;
	private FileWriter datOutput;
	
	/**
	 * CONSTRUCTOR
	 */
	public DatOutput( String datPath ) {
		this.datDescriptor = new File( datPath );

		//	init FileReader && FileWriter
		try {
			this.datInput = new FileReader( datDescriptor );
			this.datOutput = new FileWriter( datDescriptor, true );
		} catch (IOException e) {
			throw new RuntimeException( IO_EXCEPTION_ERROR );
		}
		
		readHeader();
	}
	
	/**
	 * * * writeVertex
	 * Writes the vertex for the DotGraph
	 * 
	 * @return void
	 */
	public void writeVertices( List<List<String>> vertices ) {
		String[] genes = this.genesSequence.trim().split( GENE_SEPARATOR );
		
		try {
			//	cycling over vertices
			for( int i=0; i<vertices.size(); i++ ) {
				this.datOutput.write( NEW_VERTEX_SAMPLES );
			
				//	writing the activatingBit for the correspondence with genes
				for( int j=0; j<genes.length; j++ ) {
					writeGene( vertices.get(i), genes[j] );
				}
				
				this.datOutput.write( NEW_LINE );
			}
			
			//	flushes and close the file pointer
			this.datOutput.flush();
			this.datOutput.close();
		} catch (IOException e) {
			throw new RuntimeException( IO_EXCEPTION_ERROR );
		}
	}
	
	/**
	 * * * writeGene
	 * Writes the gene referring to the current vertex
	 * 
	 * @return void
	 */
	private void writeGene( List<String> v, String gene ) {
		try {
			//	skipping empty genes
			if( gene.length() == 0 )
				return;
			
			int activatingBit = v.contains(gene) ? 1 : 0;
			
			this.datOutput.write( String.format(NEW_VERTEX_GENE, activatingBit) );
		} catch (IOException e) {
			throw new RuntimeException( IO_EXCEPTION_ERROR );
		}
	}
	
	/**
	 * * * readHeader
	 * Reads the header and extracts genes sequence
	 * 
	 * @return void
	 */
	private void readHeader() {
		long datLength = datDescriptor.length();
		char[] buffer = new char[ (int)datLength ];
		
		try {
			this.datInput.read( buffer );
			
			String bufferS = new String( buffer );
			Pattern headerPattern = Pattern.compile( HEADER_PATTERN );
			Matcher headerMatcher = headerPattern.matcher( bufferS );
			
			//	header matched
			if( headerMatcher.find() ) {
				String header = bufferS.substring(headerMatcher.start(), headerMatcher.end());
				Pattern genesPattern = Pattern.compile( HEADER_PATTERN_GENES );
				Matcher genesMatcher = genesPattern.matcher( header );
				
				//	genes in header matched
				if( genesMatcher.find() ) {
					this.genesSequence = header.substring( genesMatcher.start(), genesMatcher.end() );
					
					this.datInput.close();
				}
				//	genes in header not matched
				else
					throw new RuntimeException( GENES_MATCH_ERROR );
			}
			//	header not matched
			else
				throw new RuntimeException( HEADER_MATCH_ERROR );
		} catch (IOException e) {
			throw new RuntimeException( IO_EXCEPTION_ERROR );
		}
	}
	
}
