package controller.impl.numkeypad;

import communication.api.Message;
import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;

/**
 * This class is the implementation of the enum type NUM_KEY_PAD in {@link IAccessController}.
 *
 */
@Component
public class NumKeyPadController implements IAccessController {

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
	
	public ComponentTypes.AccessControllerType getType() {
		return ComponentTypes.AccessControllerType.NUM_KEY_PAD;
	}
	
	public Message requestIdentification() {
		//Scanner scanIn = new Scanner(System.in);
		System.out.println("Welcome to the locked door. Please input the numeric passcode: ");
		String passcode = "1234";
		System.out.println(passcode);
		//String passcode = scanIn.nextLine();
		//scanIn.close();
		return createAuthorizationRequest(passcode);
	}
	
	private Message createAuthorizationRequest(String passcode) {
		Message msg = new Message(Message.Type.ACCESS_REQ, Message.MANAGER, null);
		msg.addData(Message.Field.PASSCODE, passcode);
		return msg;
	}

	@Override
	public ComponentTypes.AuthorizationType getPreferredAuthorizationType() {
		return ComponentTypes.AuthorizationType.DB_PASSCODE;
	}

	@Override
	public ComponentTypes.AuthorizationType getAltAuthorizationType() {
		return ComponentTypes.AuthorizationType.NONE_TRUE;
	}
	
}