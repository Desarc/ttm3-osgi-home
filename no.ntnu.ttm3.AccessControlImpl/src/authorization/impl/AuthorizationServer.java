package authorization.impl;

import communication.AuthenticationToken;
import communication.Message;
import communication.Serializer;
import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import authorization.api.IAuthorizationServer;

public abstract class AuthorizationServer implements IAuthorizationServer {
	
	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;
	protected String areaCode = "testarea";
	protected String type;

	public void setUp() {
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
	
	public abstract boolean authenticate(AuthenticationToken token);

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
	}
}
