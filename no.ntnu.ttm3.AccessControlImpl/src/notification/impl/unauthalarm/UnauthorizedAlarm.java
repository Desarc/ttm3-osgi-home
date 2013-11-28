package notification.impl.unauthalarm;

import componenttypes.api.ComponentTypes;

import notification.api.IAccessNotification;

// notify on attempted unauthorized access
public class UnauthorizedAlarm  implements IAccessNotification {

	@Override
	public ComponentTypes.AccessNotification getType() {
		return ComponentTypes.AccessNotification.CONTEXT_ALARM;
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
