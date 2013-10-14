package desarc.osgi.testservice.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import desarc.osgi.testservice.ITestService;

public class TestServiceActivator implements BundleActivator {

	ServiceRegistration testServiceRegistration;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		TestServiceImpl testService = new TestServiceImpl();
		testServiceRegistration = context.registerService(ITestService.class.getName(), testService, null);
		System.out.println("TestService registered.");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		testServiceRegistration.unregister();
		System.out.println("TestService unregistered.");
	}

}
