package accesspoint.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;
import componenttypes.api.ComponentTypes;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accessPoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
	},
	provide = Object.class
)

public class AccessPointCommand extends CommunicationPoint {

	private IAccessPoint accessPointSvc;
	private String accessControllerId;
	private String accessControllerType;
	private boolean associated = false;
	private boolean open = false;
	private ComponentTypes.AccessController preferredControllerType = null;
	private ComponentTypes.AccessController altControllerType = null;
	
	private long keepaliveDelay = 1000;
	private long revokeDelay = 0;
	
	public AccessPointCommand() {
		
	}

	@Reference
	public void setAccessPoint(IAccessPoint accessPointSvc) {
		this.accessPointSvc = accessPointSvc;
		this.type = accessPointSvc.getType().name();
		this.preferredControllerType = accessPointSvc.getPreferredControllerType();
		this.altControllerType = accessPointSvc.getAltControllerType();
		this.revokeDelay = accessPointSvc.getRevokeDelay();
	}
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}

	public void run(String location, String associationKeyword) {
		this.location = location;
		this.associationKeyword = associationKeyword;
		setUp();
		while (true) {
			try {
				Thread.sleep(keepaliveDelay);
			} catch (InterruptedException e) {
				System.out.println("Sleep interrupted...");
				e.printStackTrace();
			}
			Message msg = new Message(Message.Type.KEEP_ALIVE, Message.MANAGER, this.id);
			msg.addData(Message.Field.COMPONENT_TYPE, Message.ComponentType.ACCESSPOINT.name());
			//System.out.println("Sending KEEP_ALIVE...");
			hydnaSvc.sendMessage(Serializer.serialize(msg));
		}
	}
	
	public void grantAccess() {
		accessPointSvc.grantAccess();
	}
	
	public void revokeAccess() {
		accessPointSvc.revokeAccess();
	}
	
	public String getType() {
		return accessPointSvc.getType().name();
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
			if (msg.getType().equals(Message.Type.OPEN) && associated && !open) {
				grantAccess();
				open = true;
				if (revokeDelay > 0) {
					try {
						Thread.sleep(revokeDelay);
					} catch (InterruptedException e) {
						System.out.println("Sleep interrupted...");
						e.printStackTrace();
					}
					if (open) {
						revokeAccess();
						open = false;
					}
				}
			}
			else if (msg.getType().equals(Message.Type.CLOSE) && associated) {
				revokeAccess();
				open = false;
			}
			else if (msg.getType().equals(Message.Type.NEW_ID)) {
				System.out.println("Registration confirmation from "+Message.MANAGER+"!");
				this.id = msg.getData(Message.Field.COMPONENT_ID);
				this.keepaliveDelay = Long.valueOf(msg.getData(Message.Field.TIMEOUT))*3/4;
				this.registered = true;
				System.out.println("New ID: "+this.id);
			}
			else if (msg.getType().equals(Message.Type.ASSOCIATE)) {
				this.accessControllerId = msg.getData(Message.Field.COMPONENT_ID);
				this.accessControllerType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
				this.associated = true;
				printInfo();
				this.accessPointSvc.setActive();
			}
			else if (msg.getType().equals(Message.Type.DISASSOCIATE)) {
				this.accessControllerId = null;
				this.accessControllerType = null;
				this.associated = false;
				this.accessPointSvc.setInactive();
				System.out.println("Disassociated, waiting for new association...");
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
		msg.addData(Message.Field.ASSOCIATION_KEY, this.associationKeyword);
		msg.addData(Message.Field.COMPONENT_TYPE, Message.ComponentType.ACCESSPOINT.name());
		msg.addData(Message.Field.COMPONENT_SUBTYPE, this.type);
		if (this.preferredControllerType != null) {
			msg.addData(Message.Field.PREFERRED_CONTROLLER_TYPE, this.preferredControllerType.name());
		}
		if (this.altControllerType != null) {
			msg.addData(Message.Field.ALT_CONTROLLER_TYPE, this.altControllerType.name());			
		}
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
}
