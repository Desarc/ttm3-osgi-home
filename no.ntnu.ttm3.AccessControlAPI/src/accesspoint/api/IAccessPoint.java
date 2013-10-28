package accesspoint.api;

/**
 * This is the controller of a component that is typically "blocking" unauthorized access, e.g. a door
 * 
 */
public interface IAccessPoint {

	public void activate();
	
	public String getAccessPointID();
	
	public String getAccessPointType();
	
	public void setLocation(String location);
	
	public void setPreferredControllerType(String type);
	
}
