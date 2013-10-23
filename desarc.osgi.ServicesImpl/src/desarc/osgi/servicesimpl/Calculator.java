package desarc.osgi.servicesimpl;

import desarc.osgi.servicesapi.ICalculator;
import aQute.bnd.annotation.component.*;

@Component
public class Calculator implements ICalculator{

	@Override
	public double multiply(double a, double b) {
		return a*b;
	}

}