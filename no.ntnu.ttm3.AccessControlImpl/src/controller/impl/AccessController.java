package controller.impl;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;

import java.util.Scanner;

import controller.api.IAccessController;

public abstract class AccessController implements IAccessController {

	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;
	protected String accessPoint;
	protected String areaCode = "testarea";
	protected String type;

	public void setUp() {
		this.id = this.type+System.currentTimeMillis(); 
		this.listener = new HydnaListener() {
			
			@Override
			public void systemMessage(String msg) {
				System.out.println("got sysmsg: "+msg);
			}
			
			@Override
			public void signalRecieved(String msg) {
				System.out.println("got signal: "+msg);
			}
			
			@Override
			public void messageRecieved(String msg) {
				int index;
				if ((index = msg.indexOf("areacode:")) > 0){
					setAreaCode(msg.substring(index+9));
					requestIdentification();
				}
				else if ((index = msg.indexOf("controller:")) > 0){
					int index2 = msg.indexOf("access:");
					handleAuthorizationResponse(msg.substring(index+11, index2), msg.substring(index2+7));
				}
			}
		};
		/*Scanner scanIn = new Scanner(System.in);
		System.out.println("Where is the controller located?");
	    this.location = scanIn.nextLine();
	    System.out.println("Which access point is the controller controlling?");
		this.accessPoint = scanIn.nextLine();
		scanIn.close();*/
		this.location = "bedroom";
		this.accessPoint = "testdoor";
		hydnaSvc.registerListener(this.listener);
		System.out.println("Controller "+id+" active.");
		//requestAreaCode();
		requestIdentification();
	}
	
	private void requestAreaCode() {
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/mainframe", "rwe");
		System.out.println("Connected to mainframe, requesting area code...");
		hydnaSvc.sendMessage("arearequest:"+location);
	}
	
	private void setAreaCode(String code) {
		System.out.println("Received area code: "+code);
		this.areaCode = code;
	}
	
	public abstract void requestAuthorization(String passcode);
	
	public abstract void handleAuthorizationResponse(String controller, String response);
	
	protected void grantAccess() {
		System.out.println("Granting access...");
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.accessPoint, "w");
		hydnaSvc.emitSignal("open");
	}
	
	protected void revokeAccess() {
		System.out.println("Revoking access...");
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.accessPoint, "w");
		hydnaSvc.emitSignal("close");
	}

}
