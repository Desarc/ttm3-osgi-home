package accesspoint.impl.lockeddoor;

import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;

/**
 * This class is the implementation of the enum type LOCKED_DOOR in {@link IAccessPoint}.
 *
 */
@Component
public class LockedDoor implements IAccessPoint {
	
	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 * 
	 * comment:
	 * How is the problem of finding all possible AccessPoint types solved? AccessControllers and AreaManager might need
	 * to be preconfigured to work with a specific type of AccessPoint (though this is probably more relevant for
	 * AccessController and AuthorizationServer)
	 */
	/*public String getType() {
		return getClass().getName();
	}*/
	
	public ComponentTypes.AccessPointType getType() {
		return ComponentTypes.AccessPointType.LOCKED_DOOR;
	}

	@Override
	public void grantAccess() {
		System.out.println("Door unlocked!");
	}

	@Override
	public void revokeAccess() {
		System.out.println("Door locked!");
	}

	@Override
	public ComponentTypes.AccessControllerType getPreferredControllerType() {
		return ComponentTypes.AccessControllerType.NFC;
	}

	@Override
	public ComponentTypes.AccessControllerType getAltControllerType() {
		return ComponentTypes.AccessControllerType.NUM_KEY_PAD;
	}
}
