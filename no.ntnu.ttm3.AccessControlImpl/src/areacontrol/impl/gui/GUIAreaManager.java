package areacontrol.impl.gui;

import hydna.ntnu.student.api.HydnaApi;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import areacontrol.api.IAreaManager;
import areacontrol.impl.AreaManager;

@Component
public class GUIAreaManager extends AreaManager implements IAreaManager {

	@Activate
	public void activate() {
		super.setUp();
	}
	
	@Reference
	public void setHydnaApi(HydnaApi hydna) {
		this.hydnaSvc = hydna;
	}
	
}
