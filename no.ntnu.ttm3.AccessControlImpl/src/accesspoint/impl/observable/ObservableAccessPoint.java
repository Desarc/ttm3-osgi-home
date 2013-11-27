package accesspoint.impl.observable;

import java.util.Observable;

import componenttypes.api.ComponentTypes;

import accesspoint.api.IAccessPoint;

/**
 * Accesspoint which implements the Java observer/observable pattern.
 */
public class ObservableAccessPoint extends Observable implements IAccessPoint {

	private final String type;
	private Boolean access = Boolean.FALSE;

	/**
	 * Construct a new observable access point.
	 * Because this can be used for a wide range of different access points,
	 * it does require you to provide a type.
	 * That way, multiple access point types can be implemented using this class.
	 */
	public ObservableAccessPoint(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see accesspoint.api.IAccessPoint#grantAccess()
	 */
	@Override
	public void grantAccess() {
		access = Boolean.TRUE;
		setChanged();
		notifyObservers(Boolean.TRUE);
	}

	/* (non-Javadoc)
	 * @see accesspoint.api.IAccessPoint#revokeAccess()
	 */
	@Override
	public void revokeAccess() {
		access = Boolean.FALSE;
		setChanged();
		notifyObservers(Boolean.FALSE);
	}

	/* (non-Javadoc)
	 * @see accesspoint.api.IAccessPoint#getType()
	 */
	/*@Override
	public String getType() {
		return type;
	}*/
	
	
	public boolean hasAccess() {
		return access == Boolean.TRUE;
	}

	@Override
	public ComponentTypes.AccessPoint getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public componenttypes.api.ComponentTypes.AccessController getPreferredControllerType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public componenttypes.api.ComponentTypes.AccessController getAltControllerType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getRevokeDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
