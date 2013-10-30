package controller.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.IAuthorization;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.Message;
import communication.Serializer;
import controller.api.IAccessController;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accessController",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
	},
	provide = Object.class
)

public class ControllerCommand extends CommunicationPoint {

	private IAccessController accessControllerSvc;
	private String accessPointId;
	private String authorizationType;
	
	public ControllerCommand() {
		this.accessPointId = "test";
		this.location = "testlocation";
		this.type = "test";
		this.authorizationType = IAuthorization.Type.DB_PASSCODE.toString();
	}

	@Reference
	public void setAccessController(IAccessController accessControllerSvc) {
		this.accessControllerSvc = accessControllerSvc;
	}
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void run() {
		connect();
		String identification = requestIdentification();
		Message msg = createAuthorizationRequest(identification);
		requestAuthorization(msg);
	}
	
	public void connect() {
		setUp();
	}
	
	private String requestIdentification() {
		return accessControllerSvc.requestIdentification();
	}
	
	private Message createAuthorizationRequest(String passcode){
		Message msg = new Message(Message.Type.ACCESSREQ, Message.MANAGER, this.id);
		msg.addData(Message.Field.AUTHTYPE, this.authorizationType);
		msg.addData(Message.Field.PASSCODE, passcode);
		return msg;
	}
	
	private void requestAuthorization(Message msg) {
		System.out.println("Requesting authorization...");
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
		}
		else {
			System.out.println("Passcode not OK, try again.");
			revokeAccess();
		}
	}
	
	public void grantAccess() {
		System.out.println("Granting access...");
		Message msg = new Message(Message.Type.OPEN, this.accessPointId, this.id);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public void revokeAccess() {
		System.out.println("Revoking access...");
		Message msg = new Message(Message.Type.CLOSE, this.accessPointId, this.id);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public void acInfo() {
		System.out.println("This is AccessController "+this.id+" of type "+accessControllerSvc.getType()+".");
	}
	
	public IAccessController.Type getType() {
		return accessControllerSvc.getType();
	}

	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.Type.ACCESSRSP)) {
				handleAuthorizationResponse(msg.getData(Message.Field.ACCESSRES));
			}
		}
	}
}
