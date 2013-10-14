package desarc.osgi.helloworld;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import desarc.osgi.testservice.ITestService;



public class Activator implements BundleActivator {

	ServiceTracker serviceTracker;
	ServiceReference reference;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello world!");
		System.out.println("Tracking service references for TestService...");
		TestServiceTrackerCustomizer customizer = new TestServiceTrackerCustomizer(context);
		serviceTracker = new ServiceTracker(context, ITestService.class.getName(), customizer);
		serviceTracker.open();
		ITestService testService = (ITestService) serviceTracker.getService();
		//if (testService != null) {
		//	System.out.println(testService.sayHello());
		//}
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		serviceTracker.close();
		System.out.println("Goodbye World!!");
	}

}
