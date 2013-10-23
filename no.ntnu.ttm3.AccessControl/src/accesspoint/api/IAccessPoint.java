package accesspoint.api;

import aQute.bnd.annotation.ProviderType;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
@ProviderType
public interface IAccessPoint {

	public void grantAccess();
	
	public void revokeAccess();
	
}
