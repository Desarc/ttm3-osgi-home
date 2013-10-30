package areacontrol.cmd;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.IAuthorization;
import notification.api.IAccessNotification;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.Message;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accesspoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=authInfo",
		CommandProcessor.COMMAND_FUNCTION + ":String=setLocation",
		CommandProcessor.COMMAND_FUNCTION + ":String=connect",
		CommandProcessor.COMMAND_FUNCTION + ":String=getId",
		CommandProcessor.COMMAND_FUNCTION + ":String=getAuthType",
		CommandProcessor.COMMAND_FUNCTION + ":String=getNotifType",
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
	
	public void authInfo() {
		System.out.println("This is the AreaManager for "+this.location);
	}
	
	public IAuthorization.Type getAuthType() {
		return this.authorizationSvc.getType();
	}
	
	public IAccessNotification.Type getNotifType() {
		return this.notificationSvc.getType();
	}

	@Override
	protected void handleMessage(Message msg) {
		
	}
}
