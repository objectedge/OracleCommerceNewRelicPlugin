package com.objectedge.monitoring.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import atg.core.util.StringUtils;
import atg.nucleus.GenericService;

import com.objectedge.newrelic.Component;
import com.objectedge.newrelic.Method;

/**
 * <b>ComponentMonitoringRegistry</b>
 * <p>
 * Registers or removes component from registry.
 * </p>
 * 
 * @author Chandan Kushwaha
 * 
 */
public class ComponentMonitoringRegistry extends GenericService implements
		MonitoringRegistry {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private WriteLock writeLock = lock.writeLock();

	private List<Component> components;
	private Map<String, List<String>> registeredComponents;
	private String testComponentName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.objectedge.monitoring.registry.MonitoringRegistry#addItemToMonitor
	 * (java.lang.Object)
	 */
	@Override
	public void addItemToMonitor(Object item) {
		if (item != null) {
			addComponentToMonitor(item.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.objectedge.monitoring.registry.MonitoringRegistry#removeItemToMonitor
	 * (java.lang.Object)
	 */
	@Override
	public void removeItemToMonitor(Object item) {
		if (item != null) {
			removeComponentToMonitor(item.toString());
		}
	}

	/**
	 * <p>
	 * Registers component in registry if component is not present already in
	 * registry.
	 * </p>
	 * 
	 * @param componentPath
	 *            - component path
	 */
	protected void addComponentToMonitor(String componentPath) {
		try {
			writeLock.lock();
			vlogDebug("Registering component: {0} in registry.", componentPath);
			// look for component
			Component componentObj = getComponent(componentPath);
			// if component is null create component object and add it to
			// component list
			if (componentObj == null) {
				componentObj = createComponent(componentPath);
				getComponents().add(componentObj);
				vlogDebug("Added new component:{0} in registry.", componentPath);
			} else {
				vlogDebug("Component:{0} is already registered.", componentPath);
			}
		} finally {
			writeLock.unlock();
		}
	}

	/**
	 * <p>
	 * Removes component from registry if component is found.
	 * </p>
	 * 
	 * @param componentPath
	 *            - component path
	 */
	protected void removeComponentToMonitor(String componentPath) {
		try {
			writeLock.lock();
			vlogDebug("Removing component: {0} from registry.", componentPath);
			// look for component
			Component componentObj = getComponent(componentPath);
			// if component is not null
			if (componentObj == null) {
				vlogDebug("Component: {0} is not found in registry.",
						componentPath);
			} else {
				getComponents().remove(getComponent(componentPath));
				vlogDebug("Component: {0} is removed from registry.",
						componentPath);
			}
		} finally {
			writeLock.unlock();
		}
	}

	/*
	 * Get list of method objects after treating method definitions.
	 * 
	 * @param methodDefList - list of method definition
	 * 
	 * @return list of Method object
	 */
	private List<Method> getMethods(List<String> methodDefList) {
		List<Method> methodList = new ArrayList<Method>();
		String regex = "[():]";
		String methodName = null;
		String[] splitStr = null;

		List<String> parameterType = new ArrayList<String>();
		for (String methodDef : methodDefList) {
			splitStr = methodDef.split(regex);
			methodName = splitStr[0];

			for (int i = 1; i < splitStr.length; i++) {
				parameterType.add(splitStr[i]);
			}

			Method methodObj = new Method(methodName, parameterType);
			methodList.add(methodObj);
		}
		return methodList;
	}

	/**
	 * If componentPath is already registered in registry returns component
	 * object else returns null.
	 * 
	 * @param componentPath - the component path
	 * 
	 * @return - Component object
	 */
	public Component getComponent(String componentPath) {
		Component componentObj = null;
		if (getRegisteredComponents().containsKey(componentPath)) {
			for (Component component : getComponents()) {
				if (component.getName().equals(componentPath)) {
					componentObj = component;
					break;
				}
			}
		}
		return componentObj;
	}

	/*
	 * Create component object for component path
	 * 
	 * @param componentPath - the component path
	 * 
	 * @return - Component object
	 */
	@SuppressWarnings("unchecked")
	private Component createComponent(String componentPath) {
		Component componentObj = null;
		Object obj = getRegisteredComponents().get(componentPath);
		if (obj instanceof java.lang.String) {
			componentObj = new Component(componentPath,
					getMethods(Arrays.asList(obj.toString().split(","))));
		} else {
			componentObj = new Component(componentPath,
					getMethods((List<String>) obj));
		}

		return componentObj;
	}

	public void testAddComponentToMonitor() {
		if (!StringUtils.isBlank(getTestComponentName()))
			addComponentToMonitor(getTestComponentName());
	}

	public void testRemoveComponentToMonitor() {
		if (!StringUtils.isBlank(getTestComponentName()))
			removeComponentToMonitor(getTestComponentName());
	}

	/**
	 * @return the components
	 */
	public List<Component> getComponents() {
		if (this.components == null) {
			this.components = new ArrayList<Component>();
		}
		return components;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}

	/**
	 * @return the registeredComponents
	 */
	public Map<String, List<String>> getRegisteredComponents() {
		if (this.registeredComponents == null) {
			this.registeredComponents = new HashMap<String, List<String>>();
		}
		return this.registeredComponents;
	}

	/**
	 * @param registeredComponents
	 *            the registeredComponents to set
	 */
	public void setRegisteredComponents(
			Map<String, List<String>> registeredComponents) {
		this.registeredComponents = registeredComponents;
	}

	/**
	 * @return the testComponentName
	 */
	public String getTestComponentName() {
		return testComponentName;
	}

	/**
	 * @param testComponentName
	 *            the testComponentName to set
	 */
	public void setTestComponentName(String testComponentName) {
		this.testComponentName = testComponentName;
	}
}
