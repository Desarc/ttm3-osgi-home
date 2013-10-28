package controller.impl.numkeypad;

import hydna.ntnu.student.api.HydnaApi;

import java.util.Scanner;

import controller.impl.AccessController;
import aQute.bnd.annotation.component.*;

@Component
public class NumKeyPadController extends AccessController {

	@Activate
	public void activate() {
		this.type = "NumKeyPad";
		super.setUp();
	}
	
	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}
	
	@Override
	public void requestIdentification() {
		//Scanner scanIn = new Scanner(System.in);
		System.out.println("Welcome to the locked door. Please input the numeric passcode: ");
		String passcode = "1234";
		//String passcode = scanIn.nextLine();
		requestAuthorization(passcode);
		//scanIn.close();
	}

	public void requestAuthorization(String passcode) {
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.areaCode, "rwe");
		System.out.println("Connected to authorization server, authenticating passcode...");
		hydnaSvc.sendMessage("passcode:"+passcode+";controller:"+this.id);
	}
	
	public void handleAuthorizationResponse(String controller, String response) {
		if (controller.equals(this.id)) {
			if (response.equals("ok")) {
				System.out.println("Passcode OK!.");
				grantAccess();
				long timer = System.currentTimeMillis();
				while (timer+10000 > System.currentTimeMillis()){}
				System.out.println("Timeout.");
				revokeAccess();
				requestIdentification();
			}
			else {
				System.out.println("Passcode not OK, try again.");
				requestIdentification();
			}
		}
	}
}