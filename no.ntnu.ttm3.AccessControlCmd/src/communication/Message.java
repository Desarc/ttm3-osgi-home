package communication;

import java.util.HashMap;
import java.util.Set;

public class Message {

	private Type type;
	private String to, from;
	private HashMap<Field, String> data;
	
	// message recipient
	public final static String MANAGER = "manager";
	
	// message types
	public enum Type {ACCESSRSP, ACCESSREQ, OPEN, CLOSE, REGISTER};
	
	// message data fields
	public enum Field {ACCESSRES, TOKEN, ID, PASSCODE, VALUE, LOCATION, TYPE, AUTHTYPE, COMPONENTTYPE, PREFERREDTYPE };
	
	// component types
	public enum Component {ACCESSPOINT, CONTROLLER, AUTHENTICATOR };
	
	public Message(Type type, String to, String from) {
		this.type = type;
		this.to = to;
		this.from = from;
		data = new HashMap<>();
	}
	
	public void addData(Field key, String value) {
		data.put(key, value);
	}
	
	public String getData(Field key) {
		return data.get(key);
	}
	
	public Set<Field> getKeys() {
		return data.keySet();
	}
	
	public Type getType() {
		return type;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getFrom(){
		return from;
	}
	
	
}
