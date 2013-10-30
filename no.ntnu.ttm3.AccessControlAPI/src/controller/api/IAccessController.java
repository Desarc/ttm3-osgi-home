package controller.api;

/**
 * This is an access controller for an access point. It requests identification from a user (by code, voice, NFC etc...),
 * authenticates/authorizes through an authorization server, and grants access/sends notification based on result.
 */
public interface IAccessController {
	
	public enum Type {
		NUM_KEY_PAD,
		NFC,
		MOTION_DETECT,
	}
		
	Type getType();
	
	String requestIdentification();
	
}
