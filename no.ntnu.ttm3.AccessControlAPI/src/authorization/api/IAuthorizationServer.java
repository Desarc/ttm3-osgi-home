package authorization.api;

import aQute.bnd.annotation.ProviderType;


@ProviderType
public interface IAuthorizationServer {
	
	public void authorizeAccess(boolean ok, String controller);
	
	public String getAuthorizsationServerId();
	
	public String getAuthorizsationServerType();
	
	public void setLocation(String location);
	
}
