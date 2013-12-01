package controller.impl.motiondetector;

import aQute.bnd.annotation.component.Component;
import componenttypes.api.ComponentTypes;
import controller.api.IAccessController;
import controller.api.IdentificationCallback;

@Component
public class MotionDetectController implements IAccessController {

	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	/*public String getType() {
		return getClass().getName();
	}*/
	

	@Override
	public void requestIdentification(IdentificationCallback callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public ComponentTypes.AccessController getType() {
		return ComponentTypes.AccessController.MOTION_DETECT;
	}

	@Override
	public ComponentTypes.Authorization getPreferredAuthorizationType() {
		return ComponentTypes.Authorization.TIMED;
	}

	@Override
	public ComponentTypes.Authorization getAltAuthorizationType() {
		return ComponentTypes.Authorization.NONE_TRUE;
	}

	@Override
	public void setInactive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayResult(boolean success) {
		// TODO Auto-generated method stub
		
	}

	


}
