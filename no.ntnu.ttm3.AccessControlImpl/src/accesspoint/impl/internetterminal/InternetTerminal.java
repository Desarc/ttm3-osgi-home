package accesspoint.impl.internetterminal;

import componenttypes.api.ComponentTypes;
import aQute.bnd.annotation.component.Component;
import accesspoint.api.IAccessPoint;

/**
 * This class is the implementation of the enum type INET_TERM in {@link IAccessPoint}.
 *
 */
@Component
public class InternetTerminal implements IAccessPoint {

	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	/*public String getType() {
		return getClass().getName();
	}*/
	

	public void grantAccess() {
		System.out.println("Welcome to the inernet!");
	}

	public void revokeAccess() {
		System.out.println("Disconnected.");
	}

	@Override
	public ComponentTypes.AccessPointType getType() {
		return ComponentTypes.AccessPointType.INET_TERM;
	}

	@Override
	public ComponentTypes.AccessControllerType getPreferredControllerType() {
		return ComponentTypes.AccessControllerType.USER_PASS_TERM;
	}

	@Override
	public ComponentTypes.AccessControllerType getAltControllerType() {
		return ComponentTypes.AccessControllerType.ANY;
	}


}
