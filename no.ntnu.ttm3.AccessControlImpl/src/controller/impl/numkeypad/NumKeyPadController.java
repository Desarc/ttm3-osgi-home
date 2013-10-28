package controller.impl.numkeypad;

import hydna.ntnu.student.api.HydnaApi;

import java.util.Scanner;

import communication.Message;
import communication.Serializer;

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
		System.out.println("Connected to authorization server, authenticating passcode...");
		Message msg = new Message(Message.ACCESSREQ, this.authorizationServer, this.id);
		msg.addData(Message.PASSCODE, passcode);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public void handleAuthorizationResponse(String result) {
		if (result.equals("true")) {
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