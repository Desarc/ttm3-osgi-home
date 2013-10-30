package areacontrol.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.IAuthorization;
import notification.api.IAccessNotification;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.Message;
import communication.Serializer;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=areaManager",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
	},
	provide = Object.class
)

/**
 * This class may not be needed, since we will probably bundle this with the AreaManager 
 *
 */

public class AreaManagerCommand extends CommunicationPoint {

	private IAuthorization authorizationSvc;
	private IAccessNotification notificationSvc;
	private String accessPointId;
	private String accessControllerId;

	@Reference
	public void setAuthorizationComponent(IAuthorization authorizationSvc) {
		this.authorizationSvc = authorizationSvc;
	}
	
	@Reference
	public void setNotificationComponent(IAccessNotification notificationSvc) {
		this.notificationSvc = notificationSvc;
	}
	
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void connect() {
		this.location = "testlocation";
		this.type = "test";
		this.id = "MANAGER";
		setUp();
	}
	
	public void managerInfo() {
		System.out.println("This is the AreaManager for "+this.location);
	}
	
	public IAuthorization.Type getAuthType() {
		return this.authorizationSvc.getType();
	}
	
	public IAccessNotification.Type getNotifType() {
		return this.notificationSvc.getType();
	}
	
	public void grantAccess() {
		System.out.println("Granting access...");
		Message msg = new Message(Message.Type.OPEN, this.accessPointId, Message.MANAGER);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public void revokeAccess() {
		System.out.println("Revoking access...");
		Message msg = new Message(Message.Type.CLOSE, this.accessPointId, Message.MANAGER);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}

	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(Message.MANAGER)) {
			if (msg.getType().equals(Message.Type.REGISTER)) {
				
				
			}
		}
	}
}
