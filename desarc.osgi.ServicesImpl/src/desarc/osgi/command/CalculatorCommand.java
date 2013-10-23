package desarc.osgi.command;

import org.apache.felix.service.command.CommandProcessor;

import desarc.osgi.servicesapi.ICalculator;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.component.Component;

@Component(properties = {
		CommandProcessor.COMMAND_SCOPE + ":String=example",
		CommandProcessor.COMMAND_FUNCTION + ":String=multiply"
	},
	provide = Object.class
)


public class CalculatorCommand {
	private ICalculator calculatorService;
	
	@Reference
	public void setCalculator(ICalculator calculatorService) {
		this.calculatorService = calculatorService;
	}
	
	public void multiply(double a, double b) {
		System.out.println("Result: "+calculatorService.multiply(a, b));
	}
}
