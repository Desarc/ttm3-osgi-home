package communication;

import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;

public abstract class CommunicationPoint {

	protected HydnaApi hydnaSvc;
	protected HydnaListener listener;
	protected String id;
	protected String type;
	protected String location;

	protected void setUp() {
		this.listener = new HydnaListener() {
			
			@Override
			public void systemMessage(String msg) {
				System.out.println("got sysmsg: "+msg);
			}
			
			@Override
			public void signalRecieved(String msg) {
				System.out.println("got signal: "+msg);
			}
			
			@Override
			public void messageRecieved(String msg) {
				//System.out.println("Message received in AccessPoint "+getAccessPointID()+"!");
				System.out.println(msg);
				Message m = Serializer.deserialize(msg);
				handleMessage(m);
			}
		};
		this.id = this.type+System.currentTimeMillis(); // temporary ID?
		hydnaSvc.registerListener(this.listener);
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/"+this.location, "rwe");
		registerCommunicationPoint();
	}
	
	protected abstract void handleMessage(Message msg);
	
	private void registerCommunicationPoint() {
		Message msg = new Message(Message.Type.REGISTER, Message.MANAGER, this.id);
		msg.addData(Message.Field.LOCATION, this.location);
		msg.addData(Message.Field.TYPE, this.type);
		hydnaSvc.sendMessage(Serializer.serialize(msg));
	}
	
	public String getId() {
		return this.id;
	}

	public String getType() {
		return this.type;
	}

	public void setLocation(String location) {
		this.location = location;
		System.out.println("Location set to "+location+".");
	}
	
}
