/**
 * 
 */
package formula;

/**
 * @author stykky
 *
 */
public class Theta {
	private String theta;
	private boolean isPositive;

	public Theta( String theta, boolean isPositive ) {
		this.isPositive = isPositive;
		
		if( this.isPositive )
			this.theta = theta;
		else
			this.theta = theta.substring(1);
	}
	
	/**
	 * * * getTheta
	 * Returns Theta
	 * 
	 * @return String			attribute theta
	 */
	public String getTheta() {
		return this.theta;
	}
	
	/**
	 * * * isPositive
	 * Returns if it is positive
	 * 
	 * @return String			attribute if it is positive
	 */
	public boolean isPositive() {
		return this.isPositive;
	}
	
}
