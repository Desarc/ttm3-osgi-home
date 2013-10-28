package authorization.impl.database;

import communication.AuthenticationToken;
import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Reference;
import authorization.api.IAuthorizationServer;
import authorization.impl.AuthorizationServer;

public class DBAuthorizationServer extends AuthorizationServer implements IAuthorizationServer {

	@Activate
	public void activate() {
		this.type = "DBAuth";
		super.setUp();
	}

	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}

	@Override
	public boolean authenticate(AuthenticationToken token) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
