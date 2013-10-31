package controller.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.IAuthorization;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;
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
	private IAuthorization.Type preferredAuthorizationType = null;
	private IAuthorization.Type altAuthorizationType = null;
	
	public ControllerCommand() {
		this.accessPointId = "test";
		this.location = "testlocation";
		this.preferredAuthorizationType = IAuthorization.Type.DB_PASSCODE;
		this.altAuthorizationType = IAuthorization.Type.TIMED;
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
		while (true) {
			Message msg = accessControllerSvc.requestIdentification();
			msg.setFrom(this.id);
			System.out.println("Requesting authorization...");
			hydnaSvc.sendMessage(Serializer.serialize(msg));
		}
	}
	
	public void handleAuthorizationResponse(String result) {
		if (result.equals("true")) {
			System.out.println("Authorization success!");
		}
		else {
			System.out.println("Authorization failed.");
		}
	}
	
	public void printInfo() {
		System.out.println("AccessController: "+this.id);
		System.out.println("Type: "+this.type);
		System.out.println("Controlling AccessPoint: "+this.accessPointId);
		System.out.println("AccessPoint type: "+this.accessPointType);
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
			else if (msg.getType().equals(Message.Type.NEW_ID)) {
				this.id = msg.getData(Message.Field.COMPONENT_ID);
			}
			else if (msg.getType().equals(Message.Type.ASSOCIATE)) {
				this.accessPointId = msg.getData(Message.Field.COMPONENT_ID);
				this.accessPointType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
				printInfo();
			}
		}
	}
	
	protected void registerCommunicationPoint() {
		Message msg = new Message(Message.Type.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.Field.LOCATION, this.location);
		msg.addData(Message.Field.COMPONENT_TYPE, Message.ComponentType.CONTROLLER.toString());
		msg.addData(Message.Field.COMPONENT_SUBTYPE, this.type);
		if (this.preferredAuthorizationType != null) {
			msg.addData(Message.Field.PREFERRED_AUTH_TYPE, this.preferredAuthorizationType.toString());
		}
		if (this.altAuthorizationType != null) {
			msg.addData(Message.Field.ALT_AUTH_TYPE, this.altAuthorizationType.toString());			
		}
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
}
