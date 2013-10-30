package accesspoint.impl;

public abstract class AccessPoint {
	
	public final static String LOCKED_DOOR = "lockeddoor";
	public final static String AUTOMATIC_DOOR = "automaticdoor";
	public final static String INTERNET_TERMINAL = "inetterminal";
	
	protected String type;
	
	public String getType() {
		return this.type;
	}
	
}
