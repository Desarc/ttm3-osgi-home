package accesspoint.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

import command.api.CommandModule;
import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;
import controller.api.IAccessController;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accessPoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
	},
	provide = Object.class
)

public class AccessPointCommand extends CommunicationPoint implements CommandModule {

	private IAccessPoint accessPointSvc;
	private String accessControllerId;
	private String accessControllerType;
	private IAccessController.Type preferredControllerType = null;
	private IAccessController.Type altControllerType = null;
	
	public AccessPointCommand() {
		
	}

	@Reference
	public void setAccessPoint(IAccessPoint accessPointSvc) {
		this.accessPointSvc = accessPointSvc;
		this.type = accessPointSvc.getType().toString();
		this.preferredControllerType = accessPointSvc.getPreferredControllerType();
		this.altControllerType = accessPointSvc.getAltControllerType();
	}
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void run(String location) {
		this.location = location;
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

	/* 
	 * Logic for handling incoming messages.
	 * 
	 * (non-Javadoc)
	 * @see communication.CommunicationPoint#handleMessage(communication.api.Message)
	 */
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
				this.registered = true;
			}
			else if (msg.getType().equals(Message.Type.ASSOCIATE)) {
				this.accessControllerId = msg.getData(Message.Field.COMPONENT_ID);
				this.accessControllerType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
				printInfo();
			}
		}
	}
	
	/*
	 * Register with the manager
	 * 
	 * (non-Javadoc)
	 * @see communication.CommunicationPoint#registerCommunicationPoint()
	 */
	protected void registerCommunicationPoint() {
		this.id = this.type+System.currentTimeMillis(); //temporary unique ID
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
