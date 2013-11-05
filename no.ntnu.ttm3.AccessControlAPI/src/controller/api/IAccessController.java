package controller.api;

import communication.api.Message;
import componenttypes.api.ComponentTypes;

/**
 * This is an access controller for an access point. It requests identification from a user (by code, voice, NFC etc...),
 * authenticates/authorizes through an authorization server, and grants access/sends notification based on result.
 */
public interface IAccessController {
	
	ComponentTypes.AccessController getType();
	
	Message requestIdentification();
	
	ComponentTypes.Authorization getPreferredAuthorizationType();
	
	ComponentTypes.Authorization getAltAuthorizationType();
	
}
