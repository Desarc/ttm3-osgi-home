package accesspoint.impl.automaticdoor;

import controller.api.IAccessController;
import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;

/**
 * This class is the implementation of the enum type AUTOMATIC_DOOR in {@link IAccessPoint}.
 *
 */
@Component
public class AutomaticDoor implements IAccessPoint {

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
	}


	@Override
	public Type getType() {
		return IAccessPoint.Type.AUTOMATIC_DOOR;
	}


	@Override
	public IAccessController.Type getPreferredControllerType() {
		return IAccessController.Type.MOTION_DETECT;
	}


	@Override
	public IAccessController.Type getAltControllerType() {
		return IAccessController.Type.NFC;
	}


}
