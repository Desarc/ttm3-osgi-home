package authorization.api;

public interface IAuthorization {
	
	public enum Type {
		DB_PASSCODE,
		DB_ID,
		DB_USERNAME_PASSWORD,
		TIMED,
		NONE,
	}
	
	Type getType();
	
	boolean authorize(AuthorizationToken token);
	
}
