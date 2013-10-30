package notification.impl.accesslog;

import notification.api.IAccessNotification;

// notify every time area is accessed
public class AccessLogger implements IAccessNotification {

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return IAccessNotification.Type.ACCESS_LOG;
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
