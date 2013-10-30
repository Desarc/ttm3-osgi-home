package authorization.impl;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import authorization.api.IAuthorizationServer;

public abstract class AuthorizationServer implements IAuthorizationServer {
	
	/*
	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;
	protected String type;

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
				Message m = Serializer.deSerialize(msg);
				handleMessage(m);
			}
		};
		this.id = this.type+System.currentTimeMillis();
		location = "testarea";
		hydnaSvc.registerListener(this.listener);
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
		System.out.println("AuthenticationServer "+this.id+" active.");
		registerAuthorizationServer();
	}
	

	private void registerAuthorizationServer() {
		Message msg = new Message(Message.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.LOCATION, this.location);
		msg.addData(Message.TYPE, this.type);
		msg.addData(Message.COMPONENTTYPE, Message.AUTHENTICATOR);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	private void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.ACCESSREQ)) {
				String controller = msg.getFrom();
				String type = msg.getData(Message.TOKEN);
				String id = msg.getData(Message.ID);
				String passcode = msg.getData(Message.PASSCODE);
				boolean ok = authenticate(new AuthenticationToken(controller, type, id, passcode));
				authorizeAccess(ok, controller);
			}
		}
	}
	
	protected abstract boolean authenticate(AuthenticationToken token);

	@Override
	public void authorizeAccess(boolean ok, String controller) {
		Message msg = new Message(Message.ACCESSRSP, controller, this.id);
		msg.addData(Message.ACCESSRES, ""+ok);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	@Override
	public String getAuthorizsationServerId() {
		return this.id;
	}
	
	@Override
	public String getAuthorizsationServerType() {
		return this.type;
	}
	
	@Override
	public void setLocation(String location) {
		this.location = location;
	} */
}
