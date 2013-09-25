package desarc.osgi.testservice.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import desarc.osgi.testservice.TestServiceInterface;

public class TestServiceActivator implements BundleActivator {

	ServiceRegistration testServiceRegistration;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		TestServiceImpl testService = new TestServiceImpl();
		testServiceRegistration = context.registerService(TestServiceInterface.class.getName(), testService, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		testServiceRegistration.unregister();
	}

}
