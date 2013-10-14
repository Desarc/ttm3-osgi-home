package desarc.osgi.testservice.impl;

import desarc.osgi.testservice.*;;

public class TestServiceImpl implements ITestService {

	@Override
	public String sayHello() {
		System.out.println("Using test service!");
		return "Hello";
	}

}
