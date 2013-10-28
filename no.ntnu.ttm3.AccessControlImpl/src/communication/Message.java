package communication;

import java.util.HashMap;
import java.util.Set;

public class Message {

	private String type;
	private String to, from;
	private HashMap<String, String> data;
	
	// message recipient
	public final static String MANAGER = "manager";
	
	
	// message types
	public final static String ACCESSRSP = "accessrsp";
	public final static String ACCESSREQ = "accessreq";
	public final static String OPEN = "open";
	public final static String CLOSE = "close";
	public final static String REGISTER = "register";
	
	// message data fields
	public final static String ACCESSRES = "accessres";
	public final static String TOKEN = "token";
	public final static String ID = "id";
	public final static String PASSCODE = "passcode";
	public final static String VALUE = "value";
	public final static String LOCATION = "location";
	public final static String TYPE = "type";
	public final static String PREFERREDTYPE = "preferredtype";
	
	
	public Message(String type, String to, String from) {
		this.type = type;
		this.to = to;
		this.from = from;
		data = new HashMap<>();
	}
	
	public void addData(String key, String value) {
		data.put(key, value);
	}
	
	public String getData(String key) {
		return data.get(key);
	}
	
	public Set<String> getKeys() {
		return data.keySet();
	}
	
	public String getType() {
		return type;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getFrom(){
		return from;
	}
	
	
}
