package accesspoint.impl.lockeddoor;

import java.util.Scanner;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.impl.AccessPoint;

@Component
public class LockedDoor extends AccessPoint {

	private HydnaApi hydnaSvc;
	private HydnaListener listener;
	private String id;

	@Activate
	public void activate() {
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
		System.out.println("LockedDoor activated...");
		//Scanner scanIn = new Scanner(System.in);
		//System.out.println("LockedDoor started, what is the ID of this door?: ");
		//this.id = scanIn.nextLine();
		this.id = "testdoor";
		//scanIn.close();
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+id, "r");
		System.out.println("LockedDoor "+id+" active.");
	}
	
	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}
	
	
	@Override
	public void grantAccess() {
		System.out.println("Door unlocked!");
	}

	@Override
	public void revokeAccess() {
		System.out.println("Door locked!");
	}

}
