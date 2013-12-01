package notification.impl.accesslog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import componenttypes.api.ComponentTypes;
import notification.api.IAccessNotification;
import notification.api.NotificationToken;

// log every time access is requested, with result
@Component
public class AccessLogger implements IAccessNotification {

	BufferedWriter output;
	
	@Activate
	public void openLogFile() {
		File file = new File("access-log.txt");
        try {
			output = new BufferedWriter(new FileWriter(file));
			output.write("Log file\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Deactivate
	public void closeLogFile() {
		try {
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public ComponentTypes.AccessNotification getType() {
		return ComponentTypes.AccessNotification.ACCESS_LOG;
	}

	@Override
	public void activateAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateAlarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerToken(NotificationToken token) {
		String logData = "";
		if (token.getType() == null) { // result
			logData += "Result of access request:\n";
			logData += "Time: "+token.getTimestamp()+"\n";
			logData += "Controller: "+token.getController()+"\n";
			logData += "AccessPoint: "+token.getAccessPoint()+"\n";
			logData += "Result: "+token.getResult()+"\n\n";
		}
		else {	// request
			logData += "Access request:\n";
			logData += "Time: "+token.getTimestamp()+"\n";
			logData += "Controller: "+token.getController()+"\n";
			logData += "AccessPoint: "+token.getAccessPoint()+"\n";
			logData += "Authorization type: "+token.getType().name()+"\n";
			if (token.getValue() != null) {
				logData += "Authorization data: "+token.getValue()+"\n\n";
			}
			else {
				logData += "ID: "+token.getId()+"\n";
				logData += "Passcode: "+token.getPasscode()+"\n\n";
			}
		}
		//System.out.println("Logging data: \n"+logData);
		try {
			output.write(logData);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
