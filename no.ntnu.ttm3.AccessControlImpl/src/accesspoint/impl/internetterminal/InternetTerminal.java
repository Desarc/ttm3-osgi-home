package accesspoint.impl.internetterminal;

import accesspoint.api.IAccessPoint;

public class InternetTerminal implements IAccessPoint {

	/* (non-Javadoc)
	 * Standard method for getting a String that is guaranteed unique for each type,
	 * but guaranteed the same for different versions of the same type.
	 * In this case it is safe to use #getClass(), because the fact that this code is running
	 * means we're dealing with the real object and not a composed object.
	 */
	public String getType() {
		return getClass().getName();
	}

	public void grantAccess() {
		// TODO Auto-generated method stub

	}

	public void revokeAccess() {
		// TODO Auto-generated method stub

	}


}
