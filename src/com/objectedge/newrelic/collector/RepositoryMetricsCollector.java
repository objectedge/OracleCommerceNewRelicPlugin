package com.objectedge.newrelic.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import atg.adapter.gsa.GSAItemDescriptor;
import atg.adapter.gsa.GSARepository;
import atg.adapter.gsa.RepositoryItemCacheMetricsProvider;
import atg.nucleus.Nucleus;

import com.objectedge.monitoring.registry.RepositoryMonitoringRegistry;
import com.objectedge.newrelic.json.ComponentJSON;
import com.objectedge.newrelic.json.MetricJSON;

/**
 * <b>RepositoryMetricsCollector</b>
 * <p>Responsible for collecting repository related metrics for monitoring cache statistics etc.</p>
 * @author Anand Raju
 *
 */

public class RepositoryMetricsCollector implements MetricsCollector {
	private String metricName;
	private RepositoryMonitoringRegistry repositoryMonitoringRegistry;
	private RepositoryItemCacheMetricsProvider repositoryItemCacheMetricsProvider;
	 		
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		private ReadLock readLock = lock.readLock();
		
		public RepositoryMonitoringRegistry getRepositoryMonitoringRegistry() {
			return repositoryMonitoringRegistry;
		}

		public void setRepositoryMonitoringRegistry(
				RepositoryMonitoringRegistry repositoryMonitoringRegistry) {
			this.repositoryMonitoringRegistry = repositoryMonitoringRegistry;
		}

		public RepositoryItemCacheMetricsProvider getRepositoryItemCacheMetricsProvider() {
			return repositoryItemCacheMetricsProvider;
		}

		public void setRepositoryItemCacheMetricsProvider(
				RepositoryItemCacheMetricsProvider repositoryItemCacheMetricsProvider) {
			this.repositoryItemCacheMetricsProvider = repositoryItemCacheMetricsProvider;
		}

			
		@Override
		public List<ComponentJSON> getMetrics() throws Exception {
			List<ComponentJSON> componentJSONs = new ArrayList<ComponentJSON>();
			GSARepository repository = null;
			GSAItemDescriptor itemDescriptor = null;
			String[] itemDescriptors = null;
			try {
				readLock.lock();
				for(String repositoryName : getRepositoryMonitoringRegistry().getRepositoryRegistry()){
	        		repository = (GSARepository) Nucleus.getGlobalNucleus().resolveName(repositoryName);
	        		if(repository == null){
	        			continue;
	        		}
	        		
	        		itemDescriptors = repository.getItemDescriptorNames();
	        		
	        		for(String itemDescriptorName: itemDescriptors) {
	        			itemDescriptor = (GSAItemDescriptor)repository.getItemDescriptor(itemDescriptorName);
	        			ComponentJSON componentJSON = new ComponentJSON(String.format("%s[%s]", repositoryName, itemDescriptorName), null, getCacheMetric(itemDescriptor));
	        			componentJSONs.add(componentJSON);
	        		}
	        	}
			} catch(Exception e) {
				
			} finally {
				readLock.unlock();
			}
			
			return componentJSONs;
		}

		/**
		 * @param itemDescriptor
		 * @return
		 */
		private List<MetricJSON> getCacheMetric(GSAItemDescriptor itemDescriptor) {
			List<MetricJSON> metricJSONs = new ArrayList<MetricJSON>();
			metricJSONs.add(new MetricJSON("currentSize", "count", getRepositoryItemCacheMetricsProvider().getEntryCount(itemDescriptor)));
			metricJSONs.add(new MetricJSON("totalSize", "count", getRepositoryItemCacheMetricsProvider().getItemCacheSize(itemDescriptor)));
			metricJSONs.add(new MetricJSON("totalHits", "count", getRepositoryItemCacheMetricsProvider().getHitCount(itemDescriptor)));
			metricJSONs.add(new MetricJSON("totalMisses", "count", getRepositoryItemCacheMetricsProvider().getMissCount(itemDescriptor)));
			metricJSONs.add(new MetricJSON("localEntries", "count", getRepositoryItemCacheMetricsProvider().getlocalEntryCount(itemDescriptor)));
			metricJSONs.add(new MetricJSON("hitVsMissRatio", "percent", getRepositoryItemCacheMetricsProvider().getHitVsMissRatio(itemDescriptor)));
			return metricJSONs;
		}

		public String getMetricName() {
			return metricName;
		}

		public void setMetricName(String metricName) {
			this.metricName = metricName;
		}

}

