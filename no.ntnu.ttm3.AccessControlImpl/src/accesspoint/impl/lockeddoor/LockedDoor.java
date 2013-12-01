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

	@Override
	public void activate() {
		gui.deny();
	}
}
