package accesspoint.impl.lockeddoor;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;
import accesspoint.impl.AccessPoint;

@Component
public class LockedDoor extends AccessPoint implements IAccessPoint {

	@Override
	public void activate() {
		this.type = LOCKED_DOOR;
		super.setUp();
	}
	
	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
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
