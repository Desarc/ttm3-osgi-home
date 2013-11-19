package controller.impl.numkeypad;

import javax.swing.SwingUtilities;

import communication.api.Message;
import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;
import controller.api.IdentificationCallback;

/**
 * This class is the implementation of the enum type NUM_KEY_PAD in {@link IAccessController}.
 *
 */
@Component
public class NumKeyPadController implements IAccessController {

	protected NumKeyPadGUI gui;
	private IdentificationCallback callback;

	public NumKeyPadController() {
		gui = new NumKeyPadGUI(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});
	}
	
	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	/* see comment in LockedDoor.java
	 * public String getType() {
		return getClass().getName();
	}*/
	
	public ComponentTypes.AccessController getType() {
		return ComponentTypes.AccessController.NUM_KEY_PAD;
	}
	
	public void requestIdentification(IdentificationCallback callback) {
		this.callback = callback;
		gui.activate();
	}
	
	void createAuthorizationRequest(String passcode) {
		Message msg = new Message(Message.Type.ACCESS_REQ, Message.MANAGER, null);
		msg.addData(Message.Field.PASSCODE, passcode);
		callback.callback(msg);
	}

	@Override
	public ComponentTypes.Authorization getPreferredAuthorizationType() {
		return ComponentTypes.Authorization.DB_PASSCODE;
	}

	@Override
	public ComponentTypes.Authorization getAltAuthorizationType() {
		return ComponentTypes.Authorization.NONE_TRUE;
	}

	public void dispose() {
		// TODO Indicate that the GUI is closed
	}
	
}