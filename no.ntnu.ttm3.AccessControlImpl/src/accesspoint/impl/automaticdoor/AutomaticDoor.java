package accesspoint.impl.automaticdoor;

import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;

/**
 * This class is the implementation of the enum type AUTOMATIC_DOOR in {@link IAccessPoint}.
 *
 */
@Component
public class AutomaticDoor implements IAccessPoint {

	protected AutomaticDoorGUI gui;

	public AutomaticDoor() {
		gui = new AutomaticDoorGUI(this);
	}
	
	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	/*public String getType() {
		return getClass().getName();
	}*/

	public void grantAccess() {
		System.out.println("Door opened!");
		gui.allow();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		revokeAccess();
	}

	
	public void revokeAccess() {
		System.out.println("Door closed.");
		gui.deny();
	}


	@Override
	public ComponentTypes.AccessPoint getType() {
		return ComponentTypes.AccessPoint.AUTOMATIC_DOOR;
	}


	@Override
	public ComponentTypes.AccessController getPreferredControllerType() {
		return ComponentTypes.AccessController.MOTION_DETECT;
	}


	@Override
	public ComponentTypes.AccessController getAltControllerType() {
		return ComponentTypes.AccessController.NFC;
	}


	@Override
	public void deactivate() {
		gui.reset();
	}


	@Override
	public long getRevokeDelay() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void dispose() {
		// TODO Indicate that the GUI is closed
	}

	@Override
	public void activate() {
		gui.deny();
	}


}
