package authorization.api;

import componenttypes.api.ComponentTypes;

public interface IAuthorization {
	
	ComponentTypes.AuthorizationType getType();
	
	boolean authorize(AuthorizationToken token);
	
}
