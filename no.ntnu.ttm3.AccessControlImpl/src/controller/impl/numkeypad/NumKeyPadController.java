package controller.impl.numkeypad;

import javax.swing.SwingUtilities;

import communication.api.Message;
import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;
import controller.api.IdentificationCallback;

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
	
	public ComponentTypes.AccessController getType() {
		return ComponentTypes.AccessController.NUM_KEY_PAD;
	}
	
	public void requestIdentification(IdentificationCallback callback) {
		this.callback = callback;
		gui.activate();
	}
	
	public void setInactive() {
		gui.reset();
	}
	
	void createAuthorizationRequest(String passcode) {
		Message msg = new Message(Message.Type.ACCESS_REQ, Message.MANAGER, null); // id is stored in ControllerCommand
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

	@Override
	public void displayResult(boolean success) {
		gui.displayResult(success);
		if (success) {
			System.out.println("Authorization success!");
		}
		else {
			System.out.println("Authorization failed.");
		}
	}
	
}