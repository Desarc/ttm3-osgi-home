package controller.impl.nfc;

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
	public void setInactive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayResult(boolean success) {
		// TODO Auto-generated method stub
		
	}
}