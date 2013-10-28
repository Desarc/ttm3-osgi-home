package authorization.impl.timedauth;

import hydna.ntnu.student.api.HydnaApi;
import communication.AuthenticationToken;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import authorization.impl.AuthorizationServer;

@Component
public class TimedAuthorizationServer extends AuthorizationServer{


	@Activate
	public void activate() {
		this.type = "TimedAuth";
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
