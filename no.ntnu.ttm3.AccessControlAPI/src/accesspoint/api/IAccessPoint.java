package accesspoint.api;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
public interface IAccessPoint {

	/**
	 * Indicate that the access point should provide access (e.g. not "block")
	 * until {@link #revokeAccess()} is called.
	 */
	void grantAccess();
	
	/**
	 * Indicate that the access point should not provide access (e.g. "block)
	 * until {@link #grantAccess()} is called.
	 */
	void revokeAccess();
	
	/**
	 * Return a string (the type) which is unique for this type of accesspoint.
	 * @return
	 */
	String getType();
	
}
