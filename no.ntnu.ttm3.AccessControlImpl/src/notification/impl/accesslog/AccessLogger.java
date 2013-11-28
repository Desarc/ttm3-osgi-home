package notification.impl.accesslog;

import componenttypes.api.ComponentTypes;
import notification.api.IAccessNotification;

// notify every time area is accessed
public class AccessLogger implements IAccessNotification {

	@Override
	public ComponentTypes.AccessNotification getType() {
		// TODO Auto-generated method stub
		return ComponentTypes.AccessNotification.ACCESS_LOG;
	}

	@Override
	public void registerEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateAlarm() {
		// TODO Auto-generated method stub
		
	}

	

}
