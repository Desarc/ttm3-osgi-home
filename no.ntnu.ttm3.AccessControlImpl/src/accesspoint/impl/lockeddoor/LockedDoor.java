package accesspoint.impl.lockeddoor;

import java.util.Scanner;

import communication.Message;
import communication.Serializer;
import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.impl.AccessPoint;

@Component
public class LockedDoor extends AccessPoint {

	@Activate
	public void activate() {
		//Scanner scanIn = new Scanner(System.in);
		//System.out.println("LockedDoor started, what is the ID of this door?: ");
		//this.id = scanIn.nextLine();
		this.id = "testdoor";
		//scanIn.close();
		super.setUp();
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
