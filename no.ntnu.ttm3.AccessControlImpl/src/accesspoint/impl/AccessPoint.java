package accesspoint.impl;

import communication.Message;
import communication.Serializer;
import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import accesspoint.api.IAccessPoint;

public abstract class AccessPoint implements IAccessPoint {
	
	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;

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
		hydnaSvc.registerListener(this.listener);
		this.location = "testlocation";
		System.out.println("AccessPoint "+this.id+" active.");
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
	}
	
	private void handleMessage(Message msg) {
		if (msg.getTo().equals(this.id)) {
			if (msg.getType().equals(Message.OPEN)) {
				grantAccess();
			}
			else if (msg.getType().equals(Message.CLOSE)) {
				revokeAccess();
			}
		}
	}
	
	@Override
	public abstract void grantAccess();

	@Override
	public abstract void revokeAccess();
	
}
