package authorization.cmd;

import aQute.bnd.annotation.component.Component;

import org.apache.felix.service.command.*;

@Component(properties =	{
		/* Felix GoGo Shell Commands */
		CommandProcessor.COMMAND_SCOPE + ":String=accesspoint",
		CommandProcessor.COMMAND_FUNCTION + ":String=authInfo",
		CommandProcessor.COMMAND_FUNCTION + ":String=setLocation",
		CommandProcessor.COMMAND_FUNCTION + ":String=connect",
		CommandProcessor.COMMAND_FUNCTION + ":String=getId",
		CommandProcessor.COMMAND_FUNCTION + ":String=getType",
	},
	provide = Object.class
)

/*
 * This class may not be needed, since we will probably use these service bundles with the AreaManager 
 *
 */

public class AuthorizationCommand {

}