package accesspoint.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.api.Message;

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
	
	public void apInfo() {
		System.out.println("This is AccessPoint "+this.id+" of type "+this.accessPointSvc.getType()+" at location "+this.location+".");
		System.out.println("This AccessPoint is controlled by controller "+this.accessControllerId+" of type "+this.accessControllerType);
	}
	
	public String getType() {
		return accessPointSvc.getType().toString();
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
			}
		}
	}
}
