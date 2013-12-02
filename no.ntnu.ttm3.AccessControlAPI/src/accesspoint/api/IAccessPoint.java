package accesspoint.api;

import componenttypes.api.ComponentTypes;

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
	
	void setInactive();
	
	void setActive();
	
	/**
	 * Time to wait until automatically revoking access. Set to 0 if this should not be done automatically.
	 * @return
	 */
	long getRevokeDelay();
	
	/**
	 * Return a string (the type) which is unique for this type of accesspoint.
	 * @return
	 */
	ComponentTypes.AccessPoint getType();
	
	ComponentTypes.AccessController getPreferredControllerType();
	
	ComponentTypes.AccessController getAltControllerType();
	
}
