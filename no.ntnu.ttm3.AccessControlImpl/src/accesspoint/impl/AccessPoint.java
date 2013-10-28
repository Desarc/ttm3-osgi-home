package accesspoint.impl;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import accesspoint.api.IAccessPoint;

public abstract class AccessPoint implements IAccessPoint {
	
	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;

	public void setUp() {
		this.listener = new HydnaListener() {
			
			@Override
			public void systemMessage(String msg) {
				System.out.println("got sysmsg: "+msg);
			}
			
			@Override
			public void signalRecieved(String msg) {
				if (msg.equals("open")) {
					grantAccess();
				}
				else if (msg.equals("close")) {
					revokeAccess();
				}
			}
			
			@Override
			public void messageRecieved(String msg) {
				System.out.println("got msg: "+msg);
			}
		};
		hydnaSvc.registerListener(this.listener);
		System.out.println("AccessPoint "+this.id+" active.");
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+id, "r");
	}
	
	@Override
	public abstract void grantAccess();

	@Override
	public abstract void revokeAccess();
	
}
