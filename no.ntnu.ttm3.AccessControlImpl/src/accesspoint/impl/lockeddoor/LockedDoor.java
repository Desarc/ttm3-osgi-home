package accesspoint.impl.lockeddoor;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;
import accesspoint.impl.AccessPoint;

@Component
public class LockedDoor extends AccessPoint implements IAccessPoint {
	
	@Activate
	public void activate() {
		this.type = LOCKED_DOOR;
	}	
	
	@Override
	public void grantAccess() {
		System.out.println("Door unlocked!");
	}

	@Override
	public void revokeAccess() {
		System.out.println("Door locked!");
	}
}
