package com.ws.core;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * This class extends {@link Application} class to declare provider classes.
 * 
 * @author Chandan Kushwaha
 * 
 */
public class JerseyApplication extends Application {
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> registeredClasses = new HashSet<Class<?>>();
		registeredClasses.add(MetricsResource.class);

		return registeredClasses;
	}
}