package controller.api;

/**
 * This is an access controller for an access point. It requests identification from a user (by code, voice, NFC etc...),
 * authenticates/authorizes through an authorization server, and grants access/sends notification based on result.
 */
public interface IAccessController {
	
	public String getAccessControllerId();
	
	public String getAccessControllerType();
	
	public void setLocation(String location);
	
	public void setPreferredAuthenticationType(String type);
	
	
}
