package accesspoint.impl.lockeddoor;

import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;

@Component
public class LockedDoor implements IAccessPoint {
	
	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	public String getType() {
		return getClass().getName();
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
