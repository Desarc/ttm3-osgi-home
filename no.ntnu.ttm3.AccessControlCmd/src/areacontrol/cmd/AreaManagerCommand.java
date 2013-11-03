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

import command.api.CommandModule;
import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;


@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=areaManager",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
		CommandProcessor.COMMAND_FUNCTION + ":String=types",
	},
	provide = Object.class
)

public class AreaManagerCommand extends CommunicationPoint implements CommandModule {

	//private IAuthorization authorizationSvc;
	private HashMap<IAuthorization.Type, IAuthorization> authorizationSvcs;
	//private HashMap<IAccessNotification.Type, IAccessNotification> notificationSvcs;
	private HashMap<String, ComponentEntry> accessPoints;
	private HashMap<String, ComponentEntry> accessControllers;
	private ArrayList<AccessAssociation> accessAssociations;
	
	private long timeout = 10000;

	public AreaManagerCommand() {
		authorizationSvcs = new HashMap<IAuthorization.Type, IAuthorization>();
		//notificationSvcs = new HashMap<IAccessNotification.Type, IAccessNotification>();
		accessPoints = new HashMap<String, ComponentEntry>();
		accessControllers = new HashMap<String, ComponentEntry>();
		accessAssociations = new ArrayList<AccessAssociation>();
	}
	
	// TODO: handle multiple service references
	@Reference
	public void setAuthorizationComponent(IAuthorization authorizationSvc) {
		//this.authorizationSvc = authorizationSvc;
		this.authorizationSvcs.put(authorizationSvc.getType(), authorizationSvc);
		displayAvailableAuthorizationTypes();
	}
	
	/*
	 * note: if this code is enabled, an IAccessNotification service must be available for this component to work.
	 * this is probably true for any such service references...
	 */
	/*@Reference
	public void setNotificationComponent(IAccessNotification notificationSvc) {
		//this.notificationSvc = notificationSvc;
		this.notificationSvcs.put(notificationSvc.getType(), notificationSvc);
	}*/
	
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
	}
	
	public void run(String location) {
		this.id = Message.MANAGER;
		this.type = Message.MANAGER;
		this.location = location;
		setUp();
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println("Sleep interrupted...");
				e.printStackTrace();
			}
			checkAlive();
		}
	}
	
	private void displayAvailableAuthorizationTypes() {
		System.out.println("Available authorization types:");
		for (IAuthorization.Type type : authorizationSvcs.keySet()) {
			System.out.println(type.name());
		}
	}
	
	public void printInfo() {
		System.out.println("This is the AreaManager for location "+this.location+".");
	}
	
	/*
	 * Assign a simpler ID based on component type and number of components
	 */
	private String assignId(String type, String oldId) {
		Message msg = new Message(Message.Type.NEW_ID, oldId, Message.MANAGER);
		msg.addData(Message.Field.TIMEOUT, ""+this.timeout);
		String newId = null;
		if (type.equals(Message.ComponentType.ACCESSPOINT.name())) {
			newId = Message.ComponentType.ACCESSPOINT.name()+this.accessPoints.size();
		}
		else if (type.equals(Message.ComponentType.CONTROLLER.name())) {
			newId = Message.ComponentType.CONTROLLER.name()+this.accessControllers.size();
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
	private boolean associateAccessPoint(ComponentEntry apComponent) {
		ComponentEntry acComponent = availableController(apComponent.preferred); 
		if (acComponent == null) {
			acComponent = availableController(apComponent.alt);
		}
		if (acComponent != null) {
			associate(apComponent, acComponent);
			return true;
		}
		return false;
	}
	
	/*
	 * Check if any AccessPoints are waiting to be associated, and associate with this controller if found.
	 * Assuming any controller of the right type is ok for now...
	 * If no AccessPoints are waiting, do nothing
	 */
	private boolean associateAccessController(ComponentEntry acComponent) {
		ComponentEntry apComponent = waitingAccessPoint(acComponent.type); 
		if (apComponent != null) {
			associate(apComponent, acComponent);
			return true;
		}
		return false;
	}

	/*
	 * Check if the requested authorization service is available
	 */
	private IAuthorization availableAuthorization(String type) {
		return authorizationSvcs.get(IAuthorization.Type.valueOf(type));
	}
	
	/*
	 * Send result back to controller, and to AccessPoint if access is authorized
	 */
	private void handleAuthorizationResult(String controllerId, boolean result) {
		Message msg = new Message(Message.Type.ACCESS_RSP, controllerId, Message.MANAGER);
		msg.addData(Message.Field.ACCESS_RES, ""+result);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
		if (result == true) {
			AccessAssociation aa = findAssociation(controllerId);
			if (aa != null) {
				Message msg2 = new Message(Message.Type.OPEN, aa.accessPointId, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg2));
			}
		}
	}
	
	/* 
	 * Logic for handling of incoming messages.
	 * 
	 * (non-Javadoc)
	 * @see communication.CommunicationPoint#handleMessage(communication.Message)
	 */
	@Override
	protected void handleMessage(Message msg) {
		if (msg.getTo().equals(Message.MANAGER)) {
			if (msg.getType().equals(Message.Type.REGISTER) && msg.getData(Message.Field.LOCATION).equals(this.location)) {
				String newId = assignId(msg.getData(Message.Field.COMPONENT_TYPE), msg.getFrom());
				System.out.println("New component registered: "+msg.getData(Message.Field.COMPONENT_TYPE)+" of type "+
						msg.getData(Message.Field.COMPONENT_SUBTYPE)+", assigned ID: "+newId);
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT.name())) {
					ComponentEntry component = new ComponentEntry(newId, msg.getData(Message.Field.COMPONENT_SUBTYPE), 
							msg.getData(Message.Field.PREFERRED_CONTROLLER_TYPE), msg.getData(Message.Field.ALT_CONTROLLER_TYPE));
					this.accessPoints.put(newId, component);
					associateAccessPoint(component);
				}
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER.name())) {
					ComponentEntry component = new ComponentEntry(newId, msg.getData(Message.Field.COMPONENT_SUBTYPE), 
							msg.getData(Message.Field.PREFERRED_AUTH_TYPE), msg.getData(Message.Field.ALT_AUTH_TYPE));
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
				else {
					System.out.println("Requested authorization service not available: "+msg.getData(Message.Field.AUTH_TYPE));
				}
				handleAuthorizationResult(msg.getFrom(), result);
			}
			else if (msg.getType().equals(Message.Type.KEEP_ALIVE)) {
				System.out.println("KEEP_ALIVE from "+msg.getFrom());
				System.out.println(msg.getType());
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT.name())) {
					this.accessPoints.get(msg.getFrom()).timestamp = System.currentTimeMillis();
				}
				else if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER.name())) {
					this.accessControllers.get(msg.getFrom()).timestamp = System.currentTimeMillis();
				}
			}
		}
	}
	
	protected void registerCommunicationPoint() {
		//does not need to register
		printInfo();
	}
	
	private AccessAssociation findAssociation(String id) {
		for (AccessAssociation aa : this.accessAssociations) {
			if (aa.accessControllerId.equals(id) || aa.accessPointId.equals(id)) {
				return aa;
			}
		}
		//the component is not associated
		return null;
	}
	
	private void disassociateAP(ComponentEntry ap) {
		System.out.println("No KEEP_ALIVE received from "+ap.id+", assuming dead...");
		this.accessPoints.remove(ap.id);
		AccessAssociation aa = findAssociation(ap.id);
		if (aa != null) {
			if (!associateAccessPoint(ap)) {
				Message msg = new Message(Message.Type.DISASSOCIATE, ap.id, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg));
			}
			this.accessAssociations.remove(aa);
		}
	}
	
	private void disassociateAC(ComponentEntry ac) {
		System.out.println("No KEEP_ALIVE received from "+ac.id+", assuming dead...");
		this.accessControllers.remove(ac.id);
		AccessAssociation aa = findAssociation(ac.id);
		if (aa != null) {
			if (!associateAccessController(ac)) {
				Message msg = new Message(Message.Type.DISASSOCIATE, ac.id, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg));
			}
			this.accessAssociations.remove(aa);
		}
	}
	
	private void checkAlive() {
		System.out.println("Checking timestamps...");
		ArrayList<ComponentEntry> removeList = new ArrayList<ComponentEntry>();
		long now = System.currentTimeMillis();
		for (ComponentEntry ap : this.accessPoints.values()) {
			if (ap.timestamp+timeout < now) {
				removeList.add(ap);
			}
		}
		for (ComponentEntry ap : removeList) {
			disassociateAP(ap);
		}
		removeList = new ArrayList<ComponentEntry>();
		for (ComponentEntry ac : this.accessControllers.values()) {
			if (ac.timestamp+timeout < now) {
				removeList.add(ac);
			}
		}
		for (ComponentEntry ac : removeList) {
			disassociateAC(ac);
		}
	}
	
	
	/*
	 * Some simple data containers...
	 */
	class ComponentEntry {
		String id;
		String type;
		String preferred;
		String alt;
		long timestamp;
		boolean associated;
		
		public ComponentEntry(String id, String type, String preferred, String alt) {
			this.id = id;
			this.type = type;
			this.preferred = preferred;
			this.alt = alt;
			this.timestamp = System.currentTimeMillis();
			this.associated = false;
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
