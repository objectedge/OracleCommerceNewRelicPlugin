package com.objectedge.monitoring.registry;

/**
 * Implementations interested either in adding or removing item from registry.
 * <ul>
 * <li><b>addItemToMonitor</b>: adds item to registry.</li>
 * <li><b>removeItemToMonitor</b>: removes item from registry.</li>
 * </ul>
 * 
 * @author Chandan Kushwaha
 *
 */
public interface MonitoringRegistry {
	public void addItemToMonitor(Object item);
	public void removeItemToMonitor(Object item);
}
