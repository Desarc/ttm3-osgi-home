package controller.impl.numkeypad;

import java.util.Scanner;

import controller.api.IAccessController;
import controller.impl.AccessController;
import aQute.bnd.annotation.component.*;

@Component
public class NumKeyPadController extends AccessController implements IAccessController {
	
	@Activate
	public void activate() {
		this.type = NUMKEYPAD;
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