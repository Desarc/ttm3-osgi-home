package areacontrol.impl.databaseauth;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Reference;
import areacontrol.impl.AreaManager;

public class DBAuthorizationServer extends AreaManager {

	@Activate
	public void activate() {
		this.type = "DBAuth";
		super.setUp();
	}
	
	@Override
	public boolean authenticate(int type, String value) {
		// TODO Auto-generated method stub
		return true;
	}

	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}
	
	
}
