package areacontrol.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.AuthorizationToken;
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
	private HashMap<IAuthorization.Type, IAuthorization> authorizationSvcs;
	private IAccessNotification notificationSvc;
	private HashMap<String, ComponentEntry> accessPoints;
	private HashMap<String, ComponentEntry> accessControllers;
	private ArrayList<AccessAssociation> accessAssociations;

	
	// TODO: handle multiple service references
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
	}
	
	public IAuthorization.Type getAuthType() {
		return this.authorizationSvc.getType();
	}
	
	public IAccessNotification.Type getNotifType() {
		return this.notificationSvc.getType();
	}
	
	private String assignId(String type, String oldId) {
		Message msg = new Message(Message.Type.NEW_ID, oldId, Message.MANAGER);
		String newId = null;
		if (type.equals(Message.ComponentType.ACCESSPOINT.toString())) {
			newId = Message.ComponentType.ACCESSPOINT.toString()+this.accessPoints.size();
		}
		else if (type.equals(Message.ComponentType.CONTROLLER.toString())) {
			newId = Message.ComponentType.CONTROLLER.toString()+this.accessControllers.size();
		}
		if (newId != null) {
			msg.addData(Message.Field.COMPONENT_ID, newId);
			hydnaSvc.sendMessage(Serializer.serialize(msg));
		}
		return newId;
	}
	
	/*
	 * Retrieve a waiting AccessPoint of the given type.
	 * Assuming only one association is allowed per controller and AccessPoint for now...
	 */
	private ComponentEntry waitingAccessPoint(String type) {
		// check preferred first, then alt
		for (String key : this.accessPoints.keySet()) {
			if (this.accessPoints.get(key).preferred.equals(type) && !this.accessPoints.get(key).associated) {
				return this.accessPoints.get(key);
			}
		}
		for (String key : this.accessPoints.keySet()) {
			if (this.accessPoints.get(key).alt.equals(type) && !this.accessPoints.get(key).associated) {
				return this.accessPoints.get(key);
			}
		}
		return null;
	}
	
	/*
	 * Retrieve an available controller of the given type.
	 * Assuming only one association is allowed per controller and AccessPoint for now...
	 */
	private ComponentEntry availableController(String type) {
		for (String key : this.accessControllers.keySet()) {
			if (this.accessControllers.get(key).type.equals(type) && !this.accessControllers.get(key).associated) {
				return this.accessControllers.get(key);
			}
		}
		return null;
	}
	
	/*
	 * Send ASSOCIATE messages to both parts of the association
	 */
	private void associate(ComponentEntry apComponent, ComponentEntry acComponent) {
		Message msg1 = new Message(Message.Type.ASSOCIATE, acComponent.id, Message.MANAGER);
		msg1.addData(Message.Field.COMPONENT_ID, apComponent.id);
		msg1.addData(Message.Field.COMPONENT_SUBTYPE, apComponent.type);
		hydnaSvc.sendMessage(Serializer.serialize(msg1));
		Message msg2 = new Message(Message.Type.ASSOCIATE, apComponent.id, Message.MANAGER);
		msg2.addData(Message.Field.COMPONENT_ID, acComponent.id);
		msg2.addData(Message.Field.COMPONENT_SUBTYPE, acComponent.type);
		hydnaSvc.sendMessage(Serializer.serialize(msg2));
		acComponent.associated = true;
		apComponent.associated = true;
		AccessAssociation aa = new AccessAssociation(apComponent.id, acComponent.id);
		this.accessAssociations.add(aa);
	}
	
	/*
	 * Find a suitable controller, and associate it with AccessPoint.
	 * Assuming any controller of the right type is ok for now...
	 * If no controller is available, do nothing
	 */
	private void associateAccessPoint(ComponentEntry apComponent) {
		ComponentEntry acComponent = availableController(apComponent.preferred); 
		if (acComponent == null) {
			acComponent = availableController(apComponent.alt);
		}
		if (acComponent != null) {
			associate(apComponent, acComponent);
		}
	}
	
	private void associateAccessController(ComponentEntry acComponent) {
		ComponentEntry apComponent = waitingAccessPoint(acComponent.type); 
		if (apComponent != null) {
			associate(apComponent, acComponent);
		}
	}

	/*
	 * Check if the requested authorization service is available
	 */
	private IAuthorization availableAuthorization(String type) {
		return authorizationSvcs.get(type);
	}
	
	private String findAssociatedAP(String controllerId) {
		for (AccessAssociation aa : this.accessAssociations) {
			if (aa.accessControllerId.equals(controllerId)) {
				return aa.accessPointId;
			}
		}
		return null;
	}
	
	/*
	 * Send result back to controller, and to AccessPoint if access is authorized
	 */
	private void handleAuthorizationResult(String controllerId, boolean result) {
		Message msg = new Message(Message.Type.ACCESS_RSP, controllerId, Message.MANAGER);
		msg.addData(Message.Field.ACCESS_RES, ""+result);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
		if (result == true) {
			String accessPointId = findAssociatedAP(controllerId);
			if (accessPointId != null) {
				Message msg2 = new Message(Message.Type.OPEN, accessPointId, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg2));
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see communication.CommunicationPoint#handleMessage(communication.Message)
	 * Logic for handling of incoming messages.
	 */
	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(Message.MANAGER) && msg.getData(Message.Field.LOCATION).equals(this.location)) {
			if (msg.getType().equals(Message.Type.REGISTER)) {
				String newId = assignId(msg.getData(Message.Field.COMPONENT_TYPE), msg.getData(Message.Field.COMPONENT_ID));
				ComponentEntry component = new ComponentEntry(newId, msg.getData(Message.Field.COMPONENT_SUBTYPE), 
						msg.getData(Message.Field.PREFERRED_CONTROLLER_TYPE), msg.getData(Message.Field.ALT_CONTROLLER_TYPE));
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT.toString())) {
					this.accessPoints.put(newId, component);
					associateAccessPoint(component);
				}
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER.toString())) {
					this.accessControllers.put(newId, component);
					associateAccessController(component);
				}
			}
			else if (msg.getType().equals(Message.Type.ACCESS_REQ)) {
				IAuthorization service = availableAuthorization(msg.getData(Message.Field.AUTH_TYPE));
				boolean result = false;
				if (service != null) {
					AuthorizationToken token = AuthorizationToken.generateToken(msg.getFrom(),msg.getData(Message.Field.AUTH_TYPE),
							msg.getData(Message.Field.ID), msg.getData(Message.Field.PASSCODE));
					result = service.authorize(token);
				}
				handleAuthorizationResult(msg.getFrom(), result);
			}
		}
	}
	
	class ComponentEntry {
		String id;
		String type;
		String preferred;
		String alt;
		boolean associated;
		
		public ComponentEntry(String id, String type, String preferred, String alt) {
			this.id = id;
			this.type = type;
			this.preferred = preferred;
			this.alt = alt;
			associated = false;
		}
	}
	
	class AccessAssociation {
		
		String accessPointId;
		String accessControllerId;
		
		AccessAssociation(String apId, String acId) {
			this.accessPointId = apId;
			this.accessControllerId = acId;
		}
	}
}
