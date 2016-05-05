package com.objectedge.monitoring.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import atg.nucleus.GenericService;

public class RepositoryMonitoringRegistry extends GenericService implements MonitoringRegistry {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private WriteLock writeLock = lock.writeLock();
    
    /* (non-Javadoc)
	 * @see com.objectedge.monitoring.registry.MonitoringRegistry#addItemToMonitor(java.lang.Object)
	 */
	@Override
	public void addItemToMonitor(Object item) {
		if(item != null) {
			addRepositoryToMonitor(item.toString());
		}
	}

	/* (non-Javadoc)
	 * @see com.objectedge.monitoring.registry.MonitoringRegistry#removeItemToMonitor(java.lang.Object)
	 */
	@Override
	public void removeItemToMonitor(Object item) {
		if(item != null) {
			removeRepositoryToMonitor(item.toString());
		}
	}
	
	protected void addRepositoryToMonitor(String repositoryPath){
    	try{
    	writeLock.lock();
    		getRepositoryRegistry().add(repositoryPath);
    	}finally{
    		writeLock.unlock();
    	}
    }

	protected void removeRepositoryToMonitor(String repositoryPath){
    	try{
    	writeLock.lock();
    		getRepositoryRegistry().remove(repositoryPath);
    	}finally{
    		writeLock.unlock();
    	}
    }
    
    @Override
    public void doStartService() throws atg.nucleus.ServiceException {
    	super.doStartService();
    }
    
	private List<String> repositoryRegistry;
	public List<String> getRepositoryRegistry() {
		if(repositoryRegistry==null)
			repositoryRegistry = new ArrayList<String>();
		return repositoryRegistry;
	}
	
	public void setRepositoryRegistry(List<String> repositoryRegistry) {
		this.repositoryRegistry = repositoryRegistry;
	}
}
