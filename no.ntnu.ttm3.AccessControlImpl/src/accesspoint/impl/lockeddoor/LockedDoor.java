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
		Message msg = new Message("test", "me", "you");
		msg.addData("testdata", "null!");
		String s = Serializer.serialize(msg);
		System.out.println(s);
		Message msg2 = Serializer.deSerialize(s);
		System.out.println(msg2.getData("testdata"));
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
