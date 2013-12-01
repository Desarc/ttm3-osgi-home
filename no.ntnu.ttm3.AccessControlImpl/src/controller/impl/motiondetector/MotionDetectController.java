package controller.impl.motiondetector;

import javax.swing.SwingUtilities;

import aQute.bnd.annotation.component.Component;
import communication.api.Message;
import componenttypes.api.ComponentTypes;
import controller.api.IAccessController;
import controller.api.IdentificationCallback;

@Component
public class MotionDetectController implements IAccessController {

	protected MotionDetectGUI gui;
	private IdentificationCallback callback;
	
	public MotionDetectController() {
		gui = new MotionDetectGUI(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});
	}
	
	@Override
	public void requestIdentification(IdentificationCallback callback) {
		this.callback = callback;
		gui.activate();
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
	
	public void createAuthorizationRequest() {
		Message msg = new Message(Message.Type.ACCESS_REQ, Message.MANAGER, null); // id is stored in ControllerCommand
		callback.callback(msg);
	}

	@Override
	public void setInactive() {
		gui.reset();
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
