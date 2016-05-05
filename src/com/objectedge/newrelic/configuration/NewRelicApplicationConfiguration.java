package com.objectedge.newrelic.configuration;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import atg.nucleus.GenericService;
import atg.nucleus.Nucleus;
import atg.service.ServerName;

public class NewRelicApplicationConfiguration extends GenericService{
	private String agentName;
	private String licenceKey;
	private static final String GUID = "com.objectedge.oracle.atg";
	private static final String version = "1.0.0";
	private String hostName;
	private String pid;
	
	@Override
	public void doStartService(){
		this.hostName = getServerName();
		this.pid = String.valueOf(getProcessID());
	}
	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * @return the guid
	 */
	public String getGuid() {
		return GUID;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	/**
	 * @return the licenceKey
	 */
	public String getLicenceKey() {
		return licenceKey;
	}
	/**
	 * @param licenceKey the licenceKey to set
	 */
	public void setLicenceKey(String licenceKey) {
		this.licenceKey = licenceKey;
	}
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}
	
	public String getServerName()
	{
		ServerName serverName = (ServerName) Nucleus.getGlobalNucleus().resolveName("/atg/dynamo/service/ServerName");
		if(serverName != null){
			return serverName.getServerName();
		}
	    return "";
	 }
	
	public long getProcessID() {
	    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String jvmName = bean.getName();
        long pid = Long.valueOf(jvmName.split("@")[0]);
	    return pid;
	}
	
}
