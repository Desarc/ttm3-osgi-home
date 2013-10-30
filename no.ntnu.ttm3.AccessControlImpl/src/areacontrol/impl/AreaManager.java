package areacontrol.impl;

import java.util.HashMap;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import areacontrol.api.IAreaManager;

public abstract class AreaManager {
	
	/*protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String location;
	protected HashMap<String, String> accessPoints;
	protected HashMap<String, String> controllers;
	protected HashMap<String, String> authenticators;

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
				System.out.println("Message received in MANAGER!");
				//System.out.println(msg);
				Message m = Serializer.deSerialize(msg);
				handleMessage(m);
			}
		};
		this.location = "testarea";
		hydnaSvc.registerListener(this.listener);
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
		System.out.println("MANAGER for "+location+" active.");
	}
	
	private void handleMessage(Message msg) {
		if (msg.getTo().equals(Message.MANAGER)) {
			if (msg.getType().equals(Message.REGISTER)) {
				if (msg.getData(Message.COMPONENTTYPE).equals(Message.ACCESSPOINT)) {
					this.accessPoints.put(msg.getFrom(), msg.getData(Message.TYPE));
					System.out.println("Registered AccessPoint "+msg.getFrom());
				}
				else if (msg.getData(Message.COMPONENTTYPE).equals(Message.AUTHENTICATOR)) {
					this.authenticators.put(msg.getFrom(), msg.getData(Message.TYPE));
					System.out.println("Registered Authenticator "+msg.getFrom());
				}
				else if (msg.getData(Message.COMPONENTTYPE).equals(Message.CONTROLLER)) {
					this.controllers.put(msg.getFrom(), msg.getData(Message.TYPE));
					System.out.println("Registered AccessController "+msg.getFrom());
				}
			}
		}
	}	
	
	@Override
	public void setLocation(String location) {
		this.location = location;
	}
	*/
}
