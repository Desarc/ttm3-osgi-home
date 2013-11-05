package authorization.api;

import componenttypes.api.ComponentTypes;

/**
 * Container for data used by an authorization service
 *
 */
public class AuthorizationToken {
		
	private String controller;
	private ComponentTypes.Authorization type;
	private String id;
	private String passcode;
	private String value;

	public AuthorizationToken(String controller, ComponentTypes.Authorization type) {
		this.controller = controller;
		this.type = type;
	}
	
	public AuthorizationToken(String controller, ComponentTypes.Authorization type, String value) {
		this.controller = controller;
		this.type = type;
		this.value = value;
	}
	
	public AuthorizationToken(String controller, ComponentTypes.Authorization type, String id, String passcode) {
		this.controller = controller;
		this.type = type;
		this.id = id;
		this.passcode = passcode;
	}

	public String getController() {
		return this.controller;
	}
	
	public ComponentTypes.Authorization getType() {
		return this.type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getPasscode() {
		return this.passcode;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static AuthorizationToken generateToken(String controller, String type, String id, String passcode) {
		if (type.equals(ComponentTypes.Authorization.NONE_FALSE.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.NONE_FALSE);
		}
		else if (type.equals(ComponentTypes.Authorization.NONE_TRUE.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.NONE_TRUE);
		}
		else if (type.equals(ComponentTypes.Authorization.TIMED.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.TIMED);
		}
		else if (type.equals(ComponentTypes.Authorization.DB_PASSCODE.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.DB_PASSCODE, passcode);
		}
		else if (type.equals(ComponentTypes.Authorization.DB_ID.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.DB_ID, id);
		}
		else if (type.equals(ComponentTypes.Authorization.DB_USERNAME_PASSWORD.name())) {
			return new AuthorizationToken(controller, ComponentTypes.Authorization.DB_USERNAME_PASSWORD, id, passcode);
		}
		return null;
	}
	
	public void show() {
		System.out.println("AuthorizationToken:");
		System.out.println(this.type);
		System.out.println(this.controller);
		System.out.println(this.id);
		System.out.println(this.passcode);
	}
}
