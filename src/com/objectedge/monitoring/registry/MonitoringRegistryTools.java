package com.objectedge.monitoring.registry;

import atg.core.util.StringUtils;
import atg.nucleus.ComponentEvent;
import atg.nucleus.GenericService;
import atg.nucleus.ServiceMap;

/**
 * <b>MonitoringRegistryTools</b>
 * <p>
 * It holds map of monitoring registry. It either registers or remove component
 * from registry.
 * </p>
 * There are following types of monitoring registry:
 * <ul>
 * <li>Repository Registry</li>
 * </ul>
 * 
 * @author Chandan Kushwaha
 * 
 */
public class MonitoringRegistryTools extends GenericService {
	// constants
	private static final String REGISTRY_TYPE_REPOSITORY = "repositoryRegistry";

	// map of registry name and its respective component
	private ServiceMap monitoringRegistryMap;

	public void addItemToMonitor(Object item) {
		registerOrRemoveItem(item, Boolean.FALSE);
	}

	public void removeItemToMonitor(Object item) {
		registerOrRemoveItem(item, Boolean.TRUE);
	}

	/*
	 * Either register or remove component from registry.
	 * 
	 * @param item
	 */
	private void registerOrRemoveItem(Object item, boolean isRemove) {
		if (item instanceof ComponentEvent) {
			ComponentEvent event = (ComponentEvent) item;
			final String registryType = getRegistryType(event);
			if (StringUtils.isBlank(registryType)) {
				vlogDebug(
						"Can't determine registry type for component: {0}. So skipping...",
						event.getPath());
			} else if (isRemove) {
				vlogDebug("Removing component: {0} from registry: {1}",
						event.getPath(), registryType);
				MonitoringRegistry registry = (MonitoringRegistry) getMonitoringRegistryMap()
						.get(registryType);
				registry.removeItemToMonitor(event.getPath());
			} else {
				vlogDebug("Adding component: {0} in registry: {1}",
						event.getPath(), registryType);
				MonitoringRegistry registry = (MonitoringRegistry) getMonitoringRegistryMap()
						.get(registryType);
				registry.addItemToMonitor(event.getPath());
			}
		} else {
			vlogInfo("Right now, this item is not supported.");
		}
	}

	/*
	 * Returns the registry type
	 * 
	 * @param event
	 */
	private String getRegistryType(ComponentEvent event) {
		String type = null;
		Object component = event.getComponent();

		if (component instanceof atg.adapter.gsa.GSARepository) {
			type = REGISTRY_TYPE_REPOSITORY;
		} else {
			// nothing to do
		}

		return type;
	}

	/**
	 * @return the monitoringRegistryMap
	 */
	public ServiceMap getMonitoringRegistryMap() {
		return monitoringRegistryMap;
	}

	/**
	 * @param monitoringRegistryMap
	 *            the monitoringRegistryMap to set
	 */
	public void setMonitoringRegistryMap(ServiceMap monitoringRegistryMap) {
		this.monitoringRegistryMap = monitoringRegistryMap;
	}
}
