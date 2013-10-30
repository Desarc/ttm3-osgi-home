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


/*
 * This is just a simple manager with one AccessPoint, one Controller and one AuthorizationComponent for now.
 * TODO: extend with support for more AccessPoints, Controllers and AuthorizationComponents, and associations between these.
 */
public class AreaManagerCommand extends CommunicationPoint {

	private IAuthorization authorizationSvc;
	private IAccessNotification notificationSvc;
	private String accessPointId;
	private String accessPointType;
	private String accessControllerId;
	private String accessControllerType;
	private boolean associated = false;

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
		this.id = Message.MANAGER;
		setUp();
	}
	
	public void managerInfo() {
		System.out.println("This is the AreaManager for "+this.location);
		System.out.println("Managing AccessPoint "+this.accessPointId+" controlled by controller "+this.accessControllerId);
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
	
	private void associate() {
		Message msg1 = new Message(Message.Type.ASSOCIATE, this.accessPointId, Message.MANAGER);
		msg1.addData(Message.Field.COMPONENT_ID, accessControllerId);
		msg1.addData(Message.Field.COMPONENT_SUBTYPE, accessControllerType);
		hydnaSvc.sendMessage(Serializer.serialize(msg1));
		Message msg2 = new Message(Message.Type.ASSOCIATE, this.accessControllerId, Message.MANAGER);
		msg2.addData(Message.Field.COMPONENT_ID, accessPointId);
		msg2.addData(Message.Field.COMPONENT_SUBTYPE, accessPointType);
		hydnaSvc.sendMessage(Serializer.serialize(msg2));
		associated = true;
	}

	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(Message.MANAGER)) {
			if (msg.getType().equals(Message.Type.REGISTER) && msg.getData(Message.Field.LOCATION).equals(this.location)) {
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT)) {
					this.accessPointId = msg.getData(Message.Field.COMPONENT_ID);
					this.accessPointType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
					if (this.accessControllerId != null && !associated) {
						associate();
					}
				}
				else if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER)) {
					this.accessControllerId = msg.getData(Message.Field.COMPONENT_ID);
					this.accessControllerType = msg.getData(Message.Field.COMPONENT_SUBTYPE);
					if (this.accessPointId != null && !associated) {
						associate();
					}
				}
			}
			else if (msg.getType().equals(Message.Type.ACCESS_REQ)) {
				
			}
		}
	}
}
