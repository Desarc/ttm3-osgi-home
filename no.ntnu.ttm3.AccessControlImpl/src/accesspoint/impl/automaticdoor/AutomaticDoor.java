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
		gui.setVisible(true);
	}

	public void grantAccess() {
		System.out.println("Door opened!");
		gui.allow();
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		revokeAccess();*/
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
		gui.dispose();
	}


	@Override
	public long getRevokeDelay() {
		return 10000;
	}


	public void dispose() {
		// TODO Indicate that the GUI is closed
	}

	@Override
	public void activate() {
		gui.deny();
	}


}
