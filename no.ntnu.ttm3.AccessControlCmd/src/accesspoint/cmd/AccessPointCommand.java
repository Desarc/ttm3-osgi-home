package accesspoint.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.Message;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accessPoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=connect",
	},
	provide = Object.class
)

public class AccessPointCommand extends CommunicationPoint {

	private IAccessPoint accessPointSvc;
	
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
		}
	}
}
