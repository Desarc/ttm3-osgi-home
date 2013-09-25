package com.javaworld.sample.helloworld;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import desarc.osgi.testservice.TestServiceInterface;;


public class Activator implements BundleActivator {

	ServiceReference testServiceReference;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Hello World!");
		testServiceReference = context.getServiceReference(TestServiceInterface.class.getName());
		TestServiceInterface testService = (TestServiceInterface) context.getService(testServiceReference);
		System.out.println(testService.sayHello());
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Goodbye World!!");
		context.ungetService(testServiceReference);
	}

}
