package controller.api;

import authorization.api.IAuthorization;
import communication.api.Message;

/**
 * This is an access controller for an access point. It requests identification from a user (by code, voice, NFC etc...),
 * authenticates/authorizes through an authorization server, and grants access/sends notification based on result.
 */
public interface IAccessController {
	
	/*
	 * Valid Controller types.
	 * ANY and NONE represent the choices of any or none preferred/alternate controllers for an AccessPoint,
	 * and do not describe real implementations.
	 */
	public enum Type {
		NUM_KEY_PAD,
		NFC,
		MOTION_DETECT,
		USER_PASS_TERM,
		NONE,
		ANY,
	}
		
	Type getType();
	
	Message requestIdentification();
	
	IAuthorization.Type getPreferredAuthorizationType();
	
	IAuthorization.Type getAltAuthorizationType();
	
}
