package areacontrol.api;



public interface IAreaManager {

	public void registerAccessPoint();
	
	public void registerAccessController();
	
	public void registerAlarm();
	
	public boolean authenticate(int type, String value);
	
	public void authorizeAccess(boolean ok, String controller);
}
