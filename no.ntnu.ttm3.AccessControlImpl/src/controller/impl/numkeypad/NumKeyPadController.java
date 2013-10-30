package controller.impl.numkeypad;

import aQute.bnd.annotation.component.Component;
import controller.api.IAccessController;

@Component
public class NumKeyPadController implements IAccessController {

	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	public String getType() {
		return getClass().getName();
	}
	
	public String requestIdentification() {
		//Scanner scanIn = new Scanner(System.in);
		System.out.println("Welcome to the locked door. Please input the numeric passcode: ");
		String passcode = "1234";
		System.out.println(passcode);
		//String passcode = scanIn.nextLine();
		//scanIn.close();
		return passcode;
	}
	
}