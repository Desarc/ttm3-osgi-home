package notification.api;

import aQute.bnd.annotation.ProviderType;

/**
 * This is an alarm controller. It may e.g. send an alarm/notification to some management system every time an area is accessed,
 * every time someone unauthorized attempts to access, or if an area is accessed outside of "normal" times.
 */
@ProviderType
public interface IAccessNotification {

	public void registerEvent();
	
	public void activateAlarm();
	
	public void deactivateAlarm();
	
}
