package notification.api;

import componenttypes.api.ComponentTypes;

/**
 * This is an alarm controller. It may e.g. send an alarm/notification to some management system every time an area is accessed,
 * every time someone unauthorized attempts to access, or if an area is accessed outside of "normal" times.
 */
public interface IAccessNotification {
	
	void registerToken(NotificationToken token);
	
	void activateAlarm();
	
	void deactivateAlarm();
	
	ComponentTypes.AccessNotification getType();
	
}
