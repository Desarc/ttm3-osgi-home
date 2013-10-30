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
	private String accessPointType;
	private String authorizationType;
	
	public ControllerCommand() {
		this.accessPointId = "test";
		this.location = "testlocation";
		this.authorizationType = IAuthorization.Type.DB_PASSCODE.toString();
	}

	@Reference
	public void setAccessController(IAccessController accessControllerSvc) {
		this.accessControllerSvc = accessControllerSvc;
		this.type = accessControllerSvc.getType().toString();
	}
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void run() {
		setUp();
		String identification = requestIdentification();
		Message msg = createAuthorizationRequest(identification);
		requestAuthorization(msg);
	}
	
	private String requestIdentification() {
		return accessControllerSvc.requestIdentification();
	}
	
	private Message createAuthorizationRequest(String passcode){
		Message msg = new Message(Message.Type.ACCESS_REQ, Message.MANAGER, this.id);
		msg.addData(Message.Field.AUTH_TYPE, this.authorizationType);
		msg.addData(Message.Field.PASSCODE, passcode);
		return msg;
	}
	
	private void requestAuthorization(Message msg) {
		System.out.println("Requesting authorization...");
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public void handleAuthorizationResponse(String result) {
		if (result.equals("true")) {
			System.out.println("Authorization success!");
		}
		else {
			System.out.println("Authorization failed.");
		}
	}
	
	public void acInfo() {
		System.out.println("This is AccessController "+this.id+" of type "+accessControllerSvc.getType()+".");
		System.out.println("This controller is handling AccessPoint "+this.accessPointId+" of type "+this.accessPointType);
	}
	
	public IAccessController.Type getType() {
		return accessControllerSvc.getType();
	}

	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.Type.ACCESS_RSP)) {
				handleAuthorizationResponse(msg.getData(Message.Field.ACCESS_RES));
			}
			else if (msg.getType().equals(Message.Type.ASSOCIATE)) {
				this.accessPointId = msg.getData(Message.Field.COMPONENT_ID);
				this.accessPointType = msg.getData(Message.Field.COMPONENT_TYPE);
			}
		}
	}
}
