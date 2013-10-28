package accesspoint.impl;

import communication.Message;
import communication.Serializer;
import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import accesspoint.api.IAccessPoint;

public abstract class AccessPoint implements IAccessPoint {
	
	public final static String LOCKED_DOOR = "lockeddoor";
	public final static String AUTOMATIC_DOOR = "automaticdoor";
	public final static String INTERNET_TERMINAL = "inetterminal";
	
	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String type;
	protected String controller;
	protected String preferredControllerType;
	protected String location;

	protected void setUp() {
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
				System.out.println("Message received in AccessPoint "+getAccessPointID()+"!");
				//System.out.println(msg);
				Message m = Serializer.deSerialize(msg);
				handleMessage(m);
			}
		};
		//this.id = this.type+System.currentTimeMillis();
		this.id = "testdoor";
		hydnaSvc.registerListener(this.listener);
		this.location = "testlocation";
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
		System.out.println("AccessPoint "+this.id+" active.");
		registerAccessPoint();
	}
	
	private void handleMessage(Message msg) {
		System.out.println(msg.getType());
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.OPEN)) {
				grantAccess();
			}
			else if (msg.getType().equals(Message.CLOSE)) {
				revokeAccess();
			}
		}
	}
	
	private void registerAccessPoint() {
		Message msg = new Message(Message.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.LOCATION, this.location);
		msg.addData(Message.TYPE, this.type);
		msg.addData(Message.COMPONENTTYPE, Message.ACCESSPOINT);
		msg.addData(Message.PREFERREDTYPE, this.preferredControllerType);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	protected abstract void grantAccess();

	protected abstract void revokeAccess();
	
	@Override
	public String getAccessPointID() {
		return this.id;
	}

	@Override
	public String getAccessPointType() {
		return this.type;
	}
	
	@Override
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public void setPreferredControllerType(String type) {
		this.preferredControllerType = type;
	}
	
}
