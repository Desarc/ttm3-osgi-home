package controller.impl.nfc;

import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;

@Component
public class NFCController implements IAccessController {

	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	public String getType() {
		return getClass().getName();
	}

	@Override
	public String requestIdentification() {
		// TODO Auto-generated method stub
		return null;
	}



}
