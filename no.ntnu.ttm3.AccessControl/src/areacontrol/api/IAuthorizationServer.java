package areacontrol.api;

import aQute.bnd.annotation.ProviderType;

/**
 * This is an authentication/authorization server, used by a controller.
 * May use e.g. local or remote data for authentication, time or user-type -based authorization etc...
 * 
 */
@ProviderType
public interface IAuthorizationServer {

	public void authenticate();
	
	public void authorizeAccess();
	
}
