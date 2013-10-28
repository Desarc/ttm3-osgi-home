package areacontrol.impl;

import areacontrol.api.IAreaManager;

public abstract class AreaManager implements IAreaManager {

	private String location;
	
	
	
	@Override
	public void setLocation(String location) {
		this.location = location;
	}
	
}
