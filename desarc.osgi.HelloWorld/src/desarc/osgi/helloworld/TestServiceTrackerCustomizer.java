package desarc.osgi.helloworld;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import desarc.osgi.testservice.ITestService;

public class TestServiceTrackerCustomizer implements ServiceTrackerCustomizer {

	private final BundleContext context;
	private TestServiceThread thread;
	
	public TestServiceTrackerCustomizer(BundleContext context) {
		this.context = context;
	}
	
	@Override
	public Object addingService(ServiceReference reference) {
		System.out.println("TestService is available!");
		ITestService testService = (ITestService) context.getService(reference);
		thread = new TestServiceThread(testService);
		thread.start();
		return testService;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		removedService(reference, service);
		addingService(reference);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		context.ungetService(reference);
		System.out.println("TestService no longer available.");
		thread.stopThread();
	}
	
	public static class TestServiceThread extends Thread {
		
		private volatile boolean active = true;
		private final ITestService service;
		
		public TestServiceThread(ITestService service) {
			this.service = service;
		}
		
		public void run() {
			while (active) {
				System.out.println(service.sayHello());
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		public void stopThread() {
			active = false;
		}
		
	}
	

}
