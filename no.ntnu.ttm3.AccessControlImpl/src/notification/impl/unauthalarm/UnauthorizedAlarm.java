package notification.impl.unauthalarm;

import componenttypes.api.ComponentTypes;
import notification.api.IAccessNotification;
import notification.api.NotificationToken;

// notify on attempted unauthorized access
public class UnauthorizedAlarm  implements IAccessNotification {

	@Override
	public ComponentTypes.AccessNotification getType() {
		return ComponentTypes.AccessNotification.CONTEXT_ALARM;
	}

	@Override
	public void activateAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerToken(NotificationToken token) {
		// TODO Auto-generated method stub
		
	}

	

}
