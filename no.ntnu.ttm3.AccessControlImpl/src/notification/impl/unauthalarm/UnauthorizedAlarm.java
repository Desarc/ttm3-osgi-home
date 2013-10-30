package notification.impl.unauthalarm;

import notification.api.IAccessNotification;

// notify on attempted unauthorized access
public class UnauthorizedAlarm  implements IAccessNotification {

	@Override
	public Type getType() {
		return IAccessNotification.Type.CONTEXT_ALARM;
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
