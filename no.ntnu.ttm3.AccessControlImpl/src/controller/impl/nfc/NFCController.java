package controller.impl.nfc;

import communication.api.Message;
import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;
import controller.api.IdentificationCallback;

/**
 * This class is the implementation of the enum type NFC in {@link IAccessController}.
 *
 */
@Component
public class NFCController implements IAccessController {

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
		return ComponentTypes.AccessController.NFC;
	}

	@Override
	public ComponentTypes.Authorization getPreferredAuthorizationType() {
		return ComponentTypes.Authorization.DB_ID;
	}

	@Override
	public ComponentTypes.Authorization getAltAuthorizationType() {
		return ComponentTypes.Authorization.NONE_FALSE;
	
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
}