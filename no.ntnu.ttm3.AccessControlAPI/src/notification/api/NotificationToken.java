package notification.api;

import componenttypes.api.ComponentTypes;

/**
 * Container for data used by a notification/alarm service
 *
 */
public class NotificationToken {
		
	
	private String controller;
	private String accessPoint;
	private long timestamp;
	private boolean result;
	private ComponentTypes.Authorization type;
	private String id;
	private String passcode;
	private String value;

	public NotificationToken(String controller, String accessPoint, long timestamp) {
		this.controller = controller;
		this.accessPoint = accessPoint;
		this.timestamp = timestamp;
	}
	
	public NotificationToken(String controller, String accessPoint, long timestamp, boolean result) {
		this.controller = controller;
		this.accessPoint = accessPoint;
		this.timestamp = timestamp;
		this.result = result;
	}
	
	public NotificationToken(String controller, String accessPoint, long timestamp, ComponentTypes.Authorization type, String value) {
		this.controller = controller;
		this.accessPoint = accessPoint;
		this.timestamp = timestamp;
		this.type = type;
		this.value = value;
	}
	
	public NotificationToken(String controller, String accessPoint, long timestamp, ComponentTypes.Authorization type, String id, String passcode) {
		this.controller = controller;
		this.accessPoint = accessPoint;
		this.timestamp = timestamp;
		this.type = type;
		this.id = id;
		this.passcode = passcode;
	}

	public String getController() {
		return this.controller;
	}
	
	public String getAccessPoint() {
		return this.accessPoint;
	}
	
	public long getTimestamp() {
		return this.timestamp;
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
	
	public boolean getResult() {
		return this.result;
	}
}
