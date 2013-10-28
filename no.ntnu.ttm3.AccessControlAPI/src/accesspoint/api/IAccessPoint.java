package accesspoint.api;

import aQute.bnd.annotation.ProviderType;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
@ProviderType
public interface IAccessPoint {

	public String getAccessPointID();
	
	public String getAccessPointType();
	
	public void setLocation(String location);
	
	public void setPreferredControllerType(String type);
	
}
