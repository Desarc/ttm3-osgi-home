package controller.api;

import aQute.bnd.annotation.ConsumerType;

/**
 * This is an access controller for an access point. It requests identification from a user (by code, voice, NFC etc...),
 * authenticates/authorizes through an authorization server, and grants access/sends notification based on result.
 */
@ConsumerType
public interface IAccessController {
	
	public void requestIdentification();
	
	public void requestAuthorization(String passcode);
	
	public void handleAuthorizationResponse(String controller, String response);
	
}
