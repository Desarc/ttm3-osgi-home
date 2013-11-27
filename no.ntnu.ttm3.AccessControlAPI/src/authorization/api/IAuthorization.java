package authorization.api;

import componenttypes.api.ComponentTypes;

public interface IAuthorization {
	
	ComponentTypes.Authorization getType();
	
	boolean authorize(AuthorizationToken token);
	
	void addAuthorizedValue(AuthorizationToken token);
	
	void removeAuthorizedValue(AuthorizationToken token);
	
}
