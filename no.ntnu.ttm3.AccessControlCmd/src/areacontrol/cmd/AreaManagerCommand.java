package areacontrol.cmd;

import java.util.ArrayList;
import java.util.HashMap;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.api.AuthorizationToken;
import authorization.api.IAuthorization;
import notification.api.IAccessNotification;
import notification.api.NotificationToken;

import org.apache.felix.service.command.*;

import communication.CommunicationPoint;
import communication.api.Message;
import communication.api.Serializer;
import componenttypes.api.ComponentTypes;


@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=areaManager",
		CommandProcessor.COMMAND_FUNCTION + ":String=run",
	},
	provide = Object.class
)

public class AreaManagerCommand extends CommunicationPoint {
	
	private HashMap<ComponentTypes.Authorization, IAuthorization> authorizationSvcs;
	private HashMap<ComponentTypes.AccessNotification, IAccessNotification> notificationSvcs;
	private HashMap<String, ComponentEntry> accessPoints;
	private HashMap<String, ComponentEntry> accessControllers;
	private ArrayList<AccessAssociation> accessAssociations;
	
	private long timeout = 10000;

	public AreaManagerCommand() {
		authorizationSvcs = new HashMap<ComponentTypes.Authorization, IAuthorization>();
		notificationSvcs = new HashMap<ComponentTypes.AccessNotification, IAccessNotification>();
		accessPoints = new HashMap<String, ComponentEntry>();
		accessControllers = new HashMap<String, ComponentEntry>();
		accessAssociations = new ArrayList<AccessAssociation>();
	}
	
	@Reference (type = '?', multiple = true, unbind = "removeAuthorizationComponent")
	public void setAuthorizationComponent(IAuthorization authorizationSvc) {
		this.authorizationSvcs.put(authorizationSvc.getType(), authorizationSvc);
		notifyAvailableAuthService(authorizationSvc.getType().name());
		System.out.println("New authorization type: "+authorizationSvc.getType().name());
	}
	
	public void removeAuthorizationComponent(IAuthorization authorizationSvc) {
		this.authorizationSvcs.remove(authorizationSvc.getType());
		notifyMissingAuthService(authorizationSvc.getType().name());
		System.out.println("Lost authorization type: "+authorizationSvc.getType().name());
	}
	
	@Reference (type = '?', multiple = true, unbind = "removeNotificationComponent")
	public void setNotificationComponent(IAccessNotification notificationSvc) {
		this.notificationSvcs.put(notificationSvc.getType(), notificationSvc);
		System.out.println("New notification type: "+notificationSvc.getType().name());
	}
	
	
	public void removeNotificationComponent(IAccessNotification notificationSvc) {
		this.notificationSvcs.remove(notificationSvc.getType());
		System.out.println("Lost notification type: "+notificationSvc.getType().name());
	}
	
	@Reference
	public void setHydnaSvc(HydnaApi hydnaSvc) {
		this.hydnaSvc = hydnaSvc;
		System.out.println("Hydna service added!");
	}
	
	/*@Override
	public void run() {
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
	
	public void activate(String location, String associationKeyword) {
		this.id = Message.MANAGER;
		this.type = Message.MANAGER;
		this.location = location;
		this.associationKeyword = associationKeyword;
		Thread thread = new Thread(this);
		thread.start();		
	}*/
	
	public void run(String location, String associationKeyword) {
		this.id = Message.MANAGER;
		this.type = Message.MANAGER;
		this.location = location;
		this.associationKeyword = associationKeyword;	
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
		String line = "Available authorization types: ";
		for (ComponentTypes.Authorization type : authorizationSvcs.keySet()) {
			line += type.name()+" ";
		}
		System.out.println(line);
	}
	
	private void displayAvailableNotificationTypes() {
		String line = "Available notification types: ";
		for (ComponentTypes.AccessNotification type : notificationSvcs.keySet()) {
			line += type.name()+" ";
		}
		System.out.println(line);
	}
	
	public void printInfo() {
		System.out.println("This is the AreaManager for location "+this.location+".");
		displayAvailableAuthorizationTypes();
		displayAvailableNotificationTypes();
	}
	
	/*
	 * Assign a simpler ID based on component type and number of components
	 */
	private String assignId(String type) {
		String newId = null;
		if (type.equals(Message.ComponentType.ACCESSPOINT.name())) {
			newId = Message.ComponentType.ACCESSPOINT.name()+this.accessPoints.size();
		}
		else if (type.equals(Message.ComponentType.CONTROLLER.name())) {
			newId = Message.ComponentType.CONTROLLER.name()+this.accessControllers.size();
		}
		return newId;
	}
	
	/*
	 * Retrieve a waiting AccessPoint of the given associationKey.
	 * Assuming only one association is allowed per controller and AccessPoint for now...
	 */
	private ComponentEntry availableAccessPoint(String associationKey) {
		// check preferred first, then alt
		for (String key : this.accessPoints.keySet()) {
			//if (this.accessPoints.get(key).associationKey.equals(associationKey) && !this.accessPoints.get(key).associated) {
			// assuming we can have 1..n association between AP and AC
			if (this.accessPoints.get(key).associationKey.equals(associationKey)) {
				return this.accessPoints.get(key);
			}
		}
		return null;
	}
	
	/*
	 * Retrieve an available controller with the given associationKey.
	 * Assuming only one association is allowed per controller and AccessPoint for now...
	 */
	private ComponentEntry availableController(String associationKey) {
		for (String key : this.accessControllers.keySet()) {
			if (this.accessControllers.get(key).associationKey.equals(associationKey) && !this.accessControllers.get(key).associated) {
				return this.accessControllers.get(key);
			}
		}
		return null;
	}

	
	/*
	 * If a new authorization service becomes available, notify all controllers that reqire this service.
	 */
	private void notifyAvailableAuthService(String type) {
		for (ComponentEntry ac : this.accessControllers.values()) {
			if (ac.activeType == null && (ac.preferredType.equals(type) || ac.altType.equals(type))) {
				ac.activeType = type;
				Message msg = new Message(Message.Type.CHANGE_AUTH, ac.id, Message.MANAGER);
				msg.addData(Message.Field.AUTH_TYPE, type);
				hydnaSvc.sendMessage(Serializer.serialize(msg));
			}
		}
	}
	
	/*
	 * If an authorization service becomes unavailable, assign new services to controllers.
	 */
	private void notifyMissingAuthService(String type) {
		for (ComponentEntry ac : this.accessControllers.values()) {
			if (ac.activeType.equals(type)) {
				IAuthorization available = availableAuthorization(ac.preferredType);
				if (available == null) {
					available = availableAuthorization(ac.altType);
				}
				Message msg = new Message(Message.Type.CHANGE_AUTH, ac.id, Message.MANAGER);
				if (available != null) {
					ac.activeType = available.getType().name();
					msg.addData(Message.Field.AUTH_TYPE, available.getType().name());
				}
				else {
					ac.activeType = null;
					msg.addData(Message.Field.AUTH_TYPE, ComponentTypes.Authorization.NOT_AVAILABLE.name());
				}
				hydnaSvc.sendMessage(Serializer.serialize(msg));
			}
		}
	}
	
	/*
	 * Send ASSOCIATE messages to both parts of the association
	 */
	private void associate(ComponentEntry apComponent, ComponentEntry acComponent) {
		System.out.println("Associating "+apComponent.id+" with "+acComponent.id+"...");
		Message msg1 = new Message(Message.Type.ASSOCIATE, acComponent.id, Message.MANAGER);
		msg1.addData(Message.Field.COMPONENT_ID, apComponent.id);
		msg1.addData(Message.Field.COMPONENT_SUBTYPE, apComponent.selfType);
		hydnaSvc.sendMessage(Serializer.serialize(msg1));
		Message msg2 = new Message(Message.Type.ASSOCIATE, apComponent.id, Message.MANAGER);
		msg2.addData(Message.Field.COMPONENT_ID, acComponent.id);
		msg2.addData(Message.Field.COMPONENT_SUBTYPE, acComponent.selfType);
		hydnaSvc.sendMessage(Serializer.serialize(msg2));
		acComponent.associated = true;
		apComponent.associated = true;
		AccessAssociation aa = new AccessAssociation(apComponent, acComponent);
		this.accessAssociations.add(aa);
	}
	
	/*
	 * Find a suitable controller, and associate it with AccessPoint.
	 * Association is done by associationKey
	 * If no controller is available, do nothing
	 */
	private boolean associateAccessPoint(ComponentEntry apComponent) {
		ComponentEntry acComponent = availableController(apComponent.associationKey); 
		if (acComponent != null) {
			associate(apComponent, acComponent);
			return true;
		}
		return false;
	}
	
	/*
	 * Check if any AccessPoints are waiting to be associated, and associate with this controller if found.
	 * Association is done by associationKey
	 * If no AccessPoints are waiting, do nothing
	 */
	private boolean associateAccessController(ComponentEntry acComponent) {
		ComponentEntry apComponent = availableAccessPoint(acComponent.associationKey); 
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
		return authorizationSvcs.get(ComponentTypes.Authorization.valueOf(type));
	}
	
	private void updateNotificationServices(NotificationToken token) {
		for (ComponentTypes.AccessNotification id : notificationSvcs.keySet()) {
			this.notificationSvcs.get(id).registerToken(token);
		}
	}
	
	private long handleRequestNotifications(String authType, String controller, String id, String passcode) {
		long timestamp = System.currentTimeMillis();
		String accessPoint = findAssociation(controller).accessPoint.id;
		NotificationToken token;
		if (id != null && passcode != null) {
			token = new NotificationToken(controller, accessPoint, timestamp, ComponentTypes.Authorization.valueOf(authType), id, passcode);
		}
		else if (id != null && passcode == null) {
			token = new NotificationToken(controller, accessPoint, timestamp, ComponentTypes.Authorization.valueOf(authType), id);
		}
		else if (id == null && passcode != null) {
			token = new NotificationToken(controller, accessPoint, timestamp, ComponentTypes.Authorization.valueOf(authType), passcode);
		}
		else {
			token = new NotificationToken(controller, accessPoint, timestamp, ComponentTypes.Authorization.valueOf(authType), null);
		}
		updateNotificationServices(token);
		return timestamp;
	}
	
	private void handleResultNotifications(String authType, String controller, long timestamp, boolean result) {
		String accessPoint = findAssociation(controller).accessPoint.id;
		NotificationToken token = new NotificationToken(controller, accessPoint, timestamp, result);
		updateNotificationServices(token);
	}
	
	private boolean handleAccessRequest(String authType, String controller, String id, String passcode) {
		System.out.println("New access request from "+controller+": "+authType+", "+id+" - "+passcode);
		long timestamp = handleRequestNotifications(authType, controller, id, passcode);
		IAuthorization service = availableAuthorization(authType);
		boolean result = false;
		if (service != null) {
			AuthorizationToken token = AuthorizationToken.generateToken(controller, authType, id, passcode);
			result = service.authorize(token);
		}
		else {
			System.out.println("Requested authorization service not available: "+authType);
		}
		handleResultNotifications(authType, controller, timestamp, result);
		return result;
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
				Message msg2 = new Message(Message.Type.OPEN, aa.accessPoint.id, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg2));
			}
		}
	}
	
	private void handleNewAccessPoint(String oldId, String type, String subtype, String associationKey, String preferred, String alt) {
		String newId = assignId(type);
		Message msg = new Message(Message.Type.NEW_ID, oldId, Message.MANAGER);
		msg.addData(Message.Field.TIMEOUT, ""+this.timeout);
		msg.addData(Message.Field.COMPONENT_ID, newId);
		System.out.println("New component registered: "+type+" of type "+subtype+", associationKey: "+associationKey+", assigned ID: "+newId);
		ComponentEntry component = new ComponentEntry(newId, subtype, associationKey, preferred, alt);
		this.accessPoints.put(newId, component);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
		associateAccessPoint(component);
	}
	
	private void handleNewController(String oldId, String type, String subtype, String associationKey, String preferred, String alt) {
		String newId = assignId(type);
		Message msg = new Message(Message.Type.NEW_ID, oldId, Message.MANAGER);
		msg.addData(Message.Field.TIMEOUT, ""+this.timeout);
		msg.addData(Message.Field.COMPONENT_ID, newId);
		System.out.println("New component registered: "+type+" of type "+subtype+", associationKey: "+associationKey+", assigned ID: "+newId);
		ComponentEntry component = new ComponentEntry(newId, subtype, associationKey, preferred, alt);
		if (availableAuthorization(preferred) != null) {
			msg.addData(Message.Field.AUTH_TYPE, preferred);
			component.activeType = preferred;
			// teststuff
			this.authorizationSvcs.get(ComponentTypes.Authorization.valueOf(preferred)).addAuthorizedValue(new AuthorizationToken(newId, ComponentTypes.Authorization.valueOf(preferred), "1234"));
		}
		else if (availableAuthorization(alt) != null) {
			msg.addData(Message.Field.AUTH_TYPE, alt);
			component.activeType = alt;
		}
		else {
			msg.addData(Message.Field.AUTH_TYPE, ComponentTypes.Authorization.NOT_AVAILABLE.name());
			component.activeType = ComponentTypes.Authorization.NOT_AVAILABLE.name();
		}
		System.out.println("Assigned authorization type "+component.activeType+" to "+newId);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
		this.accessControllers.put(newId, component);
		associateAccessController(component);
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
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT.name())) {
					handleNewAccessPoint(msg.getFrom(), msg.getData(Message.Field.COMPONENT_TYPE), msg.getData(Message.Field.COMPONENT_SUBTYPE),
							msg.getData(Message.Field.ASSOCIATION_KEY), msg.getData(Message.Field.PREFERRED_CONTROLLER_TYPE), msg.getData(Message.Field.ALT_CONTROLLER_TYPE));
				}
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER.name())) {
					handleNewController(msg.getFrom(), msg.getData(Message.Field.COMPONENT_TYPE), msg.getData(Message.Field.COMPONENT_SUBTYPE),
							msg.getData(Message.Field.ASSOCIATION_KEY), msg.getData(Message.Field.PREFERRED_AUTH_TYPE), msg.getData(Message.Field.ALT_AUTH_TYPE));
				}
				
			}
			else if (msg.getType().equals(Message.Type.ACCESS_REQ)) {
				boolean result = handleAccessRequest(msg.getData(Message.Field.AUTH_TYPE), msg.getFrom(),
						msg.getData(Message.Field.ID), msg.getData(Message.Field.PASSCODE));
				handleAuthorizationResult(msg.getFrom(), result);
			}
			else if (msg.getType().equals(Message.Type.KEEP_ALIVE)) {
				//System.out.println("KEEP_ALIVE from "+msg.getFrom());
				if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.ACCESSPOINT.name())) {
					if (this.accessPoints.get(msg.getFrom()) != null) {
						this.accessPoints.get(msg.getFrom()).timestamp = System.currentTimeMillis();
					}
				}
				else if (msg.getData(Message.Field.COMPONENT_TYPE).equals(Message.ComponentType.CONTROLLER.name())) {
					if (this.accessControllers.get(msg.getFrom()) != null) {
						this.accessControllers.get(msg.getFrom()).timestamp = System.currentTimeMillis();
					}
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
			if (aa.accessController.id.equals(id) || aa.accessPoint.id.equals(id)) {
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
			if (!associateAccessPoint(aa.accessController)) {
				System.out.println("Disassociating "+aa.accessController.id);
				aa.accessController.associated = false;
				Message msg = new Message(Message.Type.DISASSOCIATE, aa.accessController.id, Message.MANAGER);
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
			this.accessAssociations.remove(aa);
			if (!associateAccessPoint(aa.accessPoint)) {
				System.out.println("Dissassociating "+aa.accessPoint.id);
				if (findAssociation(aa.accessPoint.id) == null) {
					aa.accessPoint.associated = false;
				}
				Message msg = new Message(Message.Type.DISASSOCIATE, aa.accessPoint.id, Message.MANAGER);
				hydnaSvc.sendMessage(Serializer.serialize(msg));
			}
			
		}
	}
	
	private void checkAlive() {
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
		String selfType;
		String associationKey;
		String preferredType;
		String altType;
		String activeType;
		long timestamp;
		boolean associated;
		
		public ComponentEntry(String id, String type, String associationKey, String preferred, String alt) {
			this.id = id;
			this.selfType = type;
			this.associationKey = associationKey;
			this.preferredType = preferred;
			this.altType = alt;
			this.timestamp = System.currentTimeMillis();
			this.associated = false;
		}
	}
	
	class AccessAssociation {
		
		ComponentEntry accessPoint;
		ComponentEntry accessController;
		
		AccessAssociation(ComponentEntry ap, ComponentEntry ac) {
			this.accessPoint = ap;
			this.accessController = ac;
		}
	}
}
