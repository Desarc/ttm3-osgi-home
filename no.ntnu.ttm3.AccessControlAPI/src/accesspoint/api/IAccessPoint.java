package accesspoint.api;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
public interface IAccessPoint {

	void grantAccess();
	
	void revokeAccess();
	
	String getType();
	
}
