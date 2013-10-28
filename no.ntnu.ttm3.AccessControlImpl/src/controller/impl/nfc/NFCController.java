package controller.impl.nfc;

import controller.api.IAccessController;
import controller.impl.AccessController;
import aQute.bnd.annotation.component.Component;

@Component
public class NFCController extends AccessController implements IAccessController {

	@Override
	public void requestIdentification() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAuthorization(String passcode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleAuthorizationResponse(String result) {
		// TODO Auto-generated method stub
		
	}



}
