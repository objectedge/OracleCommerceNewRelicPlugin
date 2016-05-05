package com.objectedge.monitoring.agent;

import atg.nucleus.ComponentEvent;
import atg.nucleus.ComponentListener;
import atg.nucleus.GenericService;

import com.objectedge.monitoring.registry.MonitoringRegistryTools;

/**
 * <b>ComponentMonitor</b>
 * <p>
 * This class have registered to listen {@link ComponentEvent}. Whenever a
 * component is activated or deactivated it listens that event. If a component
 * is activated, it is registered to a registry and if is deactivated component
 * is removed from that registry.
 * </p>
 * 
 * @author Chandan Kushwaha
 * 
 */
public class ComponentMonitor extends GenericService implements
		ComponentListener {
	private MonitoringRegistryTools monitoringRegistryTools;

	@Override
	public void doStartService() throws atg.nucleus.ServiceException {
		super.doStartService();
		getNucleus().addComponentListener(this);
	}

	@Override
	public void componentActivated(ComponentEvent arg0) {
		getMonitoringRegistryTools().addItemToMonitor(arg0);
	}

	@Override
	public void componentDeactivated(ComponentEvent arg0) {
		getMonitoringRegistryTools().removeItemToMonitor(arg0);
	}

	/**
	 * @return the monitoringRegistryTools
	 */
	public MonitoringRegistryTools getMonitoringRegistryTools() {
		return monitoringRegistryTools;
	}

	/**
	 * @param monitoringRegistryTools
	 *            the monitoringRegistryTools to set
	 */
	public void setMonitoringRegistryTools(
			MonitoringRegistryTools monitoringRegistryTools) {
		this.monitoringRegistryTools = monitoringRegistryTools;
	}
}
