package accesspoint.cmd;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import accesspoint.api.IAccessPoint;

import org.apache.felix.service.command.*;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accesspoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=apinfo",
		CommandProcessor.COMMAND_FUNCTION + ":String=setLocation",
		CommandProcessor.COMMAND_FUNCTION + ":String=setPreferred",
		CommandProcessor.COMMAND_FUNCTION + ":String=activate",
	},
	provide = Object.class
)

public class AccessPointCommand {

	private IAccessPoint accessPointSvc;
	private String id;
	private String type;

	@Reference
	public void setAccessPoint(IAccessPoint accessPointSvc) {
		this.accessPointSvc = accessPointSvc;
		this.id = accessPointID();
		this.type = accessPointType();
	}
	
	public void activate() {
		accessPointSvc.activate();
	}

	public String accessPointID() {
		String id = accessPointSvc.getAccessPointID();
		return id;
	}
	
	public String accessPointType() {
		String type = accessPointSvc.getAccessPointType();
		return type;
	}
	
	public void setLocation(String location) {
		accessPointSvc.setLocation(location);
		System.out.println("Location set to "+location+".");
	}
	
	public void setPreferred(String type) {
		accessPointSvc.setPreferredControllerType(type);
		System.out.println("Preferred controller type set to "+type+".");
	}
	
	public void apinfo() {
		System.out.println("This is AccessController "+this.id+" of type "+this.type+".");
	}
}
