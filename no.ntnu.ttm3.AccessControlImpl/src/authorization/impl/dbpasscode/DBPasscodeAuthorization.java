package authorization.impl.dbpasscode;

import aQute.bnd.annotation.component.Component;
import authorization.api.AuthorizationToken;
import authorization.api.IAuthorization;

/**
 * This class is the implementation of the enum type DB_PASSCODE in {@link IAuthorization}.
 *
 */
@Component
public class DBPasscodeAuthorization implements IAuthorization {

	private String passcode = "1234";
	
	@Override
	public Type getType() {
		return IAuthorization.Type.DB_PASSCODE;
	}

	@Override
	public boolean authorize(AuthorizationToken token) {
		if (token.getType().equals(getType().toString())) {
			if (token.getPasscode().equals(this.passcode)) {
				return true;			
			}
		}
		return false;
	}
}
