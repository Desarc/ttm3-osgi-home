package controller.impl;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;

import communication.Message;
import communication.Serializer;
import controller.api.IAccessController;

public abstract class AccessController implements IAccessController {

	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;
	protected String accessPoint;
	protected String authorizationServer;
	protected String preferredAuthenticationType;
	protected String type;

	protected void setUp() {
		this.id = this.type+System.currentTimeMillis(); 
		this.listener = new HydnaListener() {
			
			@Override
			public void systemMessage(String msg) {
				System.out.println("got sysmsg: "+msg);
			}
			
			@Override
			public void signalRecieved(String msg) {
				System.out.println("got signal: "+msg);
			}
			
			@Override
			public void messageRecieved(String msg) {
				System.out.println("Message received in AccessController!");
				System.out.println(msg);
				Message m = Serializer.deSerialize(msg);
				handleMessage(m);
			}
		};
		this.location = "testlocation";
		this.accessPoint = "testdoor";
		hydnaSvc.registerListener(this.listener);
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
		System.out.println("Controller "+id+" active.");
		registerAccessController();
		requestIdentification();
	}
	
	private void registerAccessController() {
		Message msg = new Message(Message.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.LOCATION, this.location);
		msg.addData(Message.TYPE, this.type);
		msg.addData(Message.PREFERREDTYPE, this.preferredAuthenticationType);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	private void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.ACCESSRSP)) {
				handleAuthorizationResponse(msg.getData(Message.ACCESSRES));
			}
		}
	}
	
	protected abstract void requestIdentification();
	
	protected abstract void requestAuthorization(String passcode);
	
	protected abstract void handleAuthorizationResponse(String result);
	
	protected void grantAccess() {
		System.out.println("Granting access...");
		Message msg = new Message(Message.OPEN, this.accessPoint, this.id);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	protected void revokeAccess() {
		System.out.println("Revoking access...");
		Message msg = new Message(Message.CLOSE, this.accessPoint, this.id);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public String getAccessControllerId() {
		return this.id;
	}
	
	public String getAccessControllerType() {
		return this.type;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setPreferredAuthenticationType(String type) {
		this.preferredAuthenticationType = type;
	}

}
