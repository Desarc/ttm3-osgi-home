package mainframe.impl;

import mainframe.api.IMainframe;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import hydna.ntnu.student.api.HydnaApi;
import hydna.ntnu.student.listener.api.HydnaListener;

@Component
public class Mainframe implements IMainframe {

	private HydnaApi hydnaSvc;
	private HydnaListener listener;
	private String areaCode = "testarea";

	@Activate
	public void setUp() {
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
				int index;
				if ((index = msg.indexOf("arearequest:")) > 0){
					if (msg.substring(index+12).equals("location:")) {
						hydnaSvc.sendMessage("areacode:"+areaCode);
					}
				}
			}
		};
		hydnaSvc.registerListener(this.listener);
		hydnaSvc.stayConnected(true);
		hydnaSvc.connectChannel("ttm3-access-control.hydna.net/mainframe", "rwe");
	}
	
	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}
	
}
