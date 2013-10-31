package accesspoint.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;
import controller.api.IAccessController;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accessPoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=connect",
	},
	provide = Object.class
)

public class AccessPointCommand extends CommunicationPoint {

	private IAccessPoint accessPointSvc;
	private String accessControllerId;
	private String accessControllerType;
	private IAccessController.Type preferredControllerType = null;
	private IAccessController.Type altControllerType = null;
	
	public AccessPointCommand() {
		this.location = "testlocation";
		this.type = "test";
		this.id = "test";
	}

	@Reference
	public void setAccessPoint(IAccessPoint accessPointSvc) {
		this.accessPointSvc = accessPointSvc;
	}
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void connect() {
		setUp();
	}
	
	public void grantAccess() {
		accessPointSvc.grantAccess();
	}
	
	public void revokeAccess() {
		accessPointSvc.revokeAccess();
	}
	
	public String getType() {
		return accessPointSvc.getType().toString();
	}
	
	public void printInfo() {
		System.out.println("AccessPoint: "+this.id);
		System.out.println("Type: "+this.type);
		System.out.println("Controlled by: "+this.accessControllerId);
		System.out.println("Controller type: "+this.accessControllerType);
	}

	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.Type.OPEN)) {
				grantAccess();
			}
			else if (msg.getType().equals(Message.Type.CLOSE)) {
				revokeAccess();
			}
			else if (msg.getType().equals(Message.Type.NEW_ID)) {
				this.id = msg.getData(Message.Field.COMPONENT_ID);
			}
			else if (msg.getType().equals(Message.Type.ASSOCIATE)) {
				this.accessControllerId = msg.getData(Message.Field.COMPONENT_ID);
				this.accessControllerType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
				printInfo();
			}
		}
	}
	
	protected void registerCommunicationPoint() {
		Message msg = new Message(Message.Type.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.Field.LOCATION, this.location);
		msg.addData(Message.Field.COMPONENT_TYPE, Message.ComponentType.ACCESSPOINT.toString());
		msg.addData(Message.Field.COMPONENT_SUBTYPE, this.type);
		if (this.preferredControllerType != null) {
			msg.addData(Message.Field.PREFERRED_CONTROLLER_TYPE, this.preferredControllerType.toString());
		}
		if (this.altControllerType != null) {
			msg.addData(Message.Field.ALT_CONTROLLER_TYPE, this.altControllerType.toString());			
		}
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
}
