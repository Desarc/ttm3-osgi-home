package areacontrol.impl;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;

import java.util.Scanner;

import areacontrol.api.IAreaManager;

public abstract class AreaManager implements IAreaManager {

	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String location;
	protected String areaCode = "testarea";
	protected String type;
	
	protected final static int NONE = 0;
	protected final static int USERNAME_PASSWORD = 1;
	protected final static int PASSCODE = 2;
	

	public void setUp() {
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
				int index;
				if ((index = msg.indexOf("areacode:")) > 0){
					setAreaCode(msg.substring(index+9));
					registerManager();
					connectArea();
				}
				else if ((index = msg.indexOf("passcode:")) > 0){
					int index2 = msg.indexOf("controller:");
					if (authenticate(PASSCODE, msg.substring(index+9, index2))){
						authorizeAccess(true, msg.substring(index2+10));
					}
					else {
						authorizeAccess(false, msg.substring(index2+10));
					}
				}
			}
		};
		/*Scanner scanIn = new Scanner(System.in);
		System.out.println("Which area is this server managing?");
	    this.location = scanIn.nextLine();
		scanIn.close();*/
		location = "bedroom";
		hydnaSvc.registerListener(this.listener);
		System.out.println("AreaManager "+this.id+" active.");
		//requestAreaCode();
		connectArea();
	}
	
	private void requestAreaCode() {
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/mainframe", "rwe");
		System.out.println("Connected to mainframe, requesting area code...");
		hydnaSvc.sendMessage("arearequest:"+location);
	}
	
	private void setAreaCode(String code) {
		System.out.println("Received area code: "+code);
		this.areaCode = code;
	}
	
	private void registerManager() {
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/mainframe", "rwe");
		System.out.println("Registering as manager for "+location);
		hydnaSvc.sendMessage("manager:"+id+";location"+location);
	}
	
	private void connectArea() {
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.areaCode, "rwe");
	}
	
	
	@Override
	public void registerAccessPoint() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerAccessController() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerAlarm() {
		// TODO Auto-generated method stub

	}

	@Override
	public abstract boolean authenticate(int type, String value);

	@Override
	public void authorizeAccess(boolean ok, String controller) {
		hydnaSvc.sendMessage("controller:"+controller+"access:"+ok);
	}
	
}
