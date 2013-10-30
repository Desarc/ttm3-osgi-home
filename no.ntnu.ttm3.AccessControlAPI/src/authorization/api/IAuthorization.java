package authorization.api;

public interface IAuthorization {
	
	public enum Type {
		DB_PASSCODE,
		TIMED,
		ALWAYS_OK,
	}
	
	Type getType();
	
	
	
}
