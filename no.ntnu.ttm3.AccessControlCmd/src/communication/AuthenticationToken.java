package communication;

public class AuthenticationToken {
	
	public static final String NUMCODE = "numcode";
	public static final String USER_PASS = "userpass";
	public static final String NONE = "none";
	public static final String ID = "id";
	
	private String controller;
	private String type;
	private String id;
	private String passcode;

	public AuthenticationToken(String controller, String type, String id, String passcode) {
		this.controller = controller;
		this.type = type;
		this.id = id;
		this.passcode = passcode;
	}

	public String getController() {
		return this.controller;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getPasscode() {
		return this.passcode;
	}
	
}
