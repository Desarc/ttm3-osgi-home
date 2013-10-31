package authorization.api;

public interface IAuthorization {
	
	/*
	 * Valid Authorization types.
	 * ANY and NONE_TRUE/FALSE represent the choices of any or none preferred/alternate authorization services for a Controller,
	 * and do not describe real implementations. NONE_TRUE means that access should always be granted, while NONE_FALSE means
	 * access should never be granted. Use examples could be to ensure denial of access if the preferred authorization service
	 * fails (NONE_FALSE), or the opposite.
	 */
	public enum Type {
		DB_PASSCODE,
		DB_ID,
		DB_USERNAME_PASSWORD,
		TIMED,
		NONE_TRUE,
		NONE_FALSE,
		ANY,
	}
	
	Type getType();
	
	boolean authorize(AuthorizationToken token);
	
}
