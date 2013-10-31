package accesspoint.api;

import controller.api.IAccessController;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
public interface IAccessPoint {

	/*
	 * Valid AccessPoint types.
	 */
	public enum Type {
		LOCKED_DOOR,
		AUTOMATIC_DOOR,
		INET_TERM,
	}
	
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
	IAccessPoint.Type getType();
	
	IAccessController.Type getPreferredControllerType();
	
	IAccessController.Type getAltControllerType();
	
}
