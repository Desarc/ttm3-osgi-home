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
	
	protected LockedDoorGUI gui;

	public LockedDoor() {
		gui = new LockedDoorGUI(this);
	}
	
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
	
	public ComponentTypes.AccessPoint getType() {
		return ComponentTypes.AccessPoint.LOCKED_DOOR;
	}

	@Override
	public void grantAccess() {
		System.out.println("Door unlocked!");
		gui.allow();
	}

	@Override
	public void revokeAccess() {
		System.out.println("Door locked!");
		gui.deny();
	}
	
	//@Override
	public long getRevokeDelay() {
		return 5000;
	}

	@Override
	public ComponentTypes.AccessController getPreferredControllerType() {
		return ComponentTypes.AccessController.NFC;
	}

	@Override
	public ComponentTypes.AccessController getAltControllerType() {
		return ComponentTypes.AccessController.NUM_KEY_PAD;
	}

	@Override
	public void deactivate() {
		gui.dispose();
	}

	public void dispose() {
		// TODO Indicate that the GUI is closed
	}
}
