package controller.impl.motiondetector;

import aQute.bnd.annotation.component.Component;
import authorization.api.IAuthorization;
import communication.api.Message;
import controller.api.IAccessController;

/**
 * This class is the implementation of the enum type MOTION_DETECT in {@link IAccessController}.
 *
 */
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
	public Message requestIdentification() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		return IAccessController.Type.MOTION_DETECT;
	}

	@Override
	public IAuthorization.Type getPreferredAuthorizationType() {
		return IAuthorization.Type.TIMED;
	}

	@Override
	public IAuthorization.Type getAltAuthorizationType() {
		return IAuthorization.Type.NONE_TRUE;
	}

	


}
