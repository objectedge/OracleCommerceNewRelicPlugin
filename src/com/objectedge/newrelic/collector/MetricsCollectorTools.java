/**
 * 
 */
package com.objectedge.newrelic.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.objectedge.newrelic.configuration.NewRelicApplicationConfiguration;
import com.objectedge.newrelic.json.ComponentJSON;
import com.objectedge.newrelic.json.MetricJSON;
import com.objectedge.newrelic.json.MetricsResponseJSON;

import atg.nucleus.GenericService;
import atg.nucleus.Nucleus;
import atg.nucleus.ServiceMap;

/**
 * <b>MetricsCollectorTools</b> 
 * <p>This is a utility class which holds list of
 * collectors and invoke their getMetrics() method to collect metrics.</p>
 * 
 * @author Chandan Kushwaha
 * 
 */
public class MetricsCollectorTools extends GenericService {
	
	//Metric Aliasing allows provides a way to add Alias names for the metrics that are reported
	private boolean allowMetricAliasing = true;
	private NewRelicApplicationConfiguration configuration;
	// map of collector components
	private ServiceMap metricsCollectorsMap;

	/**
	 * It collects metrics from all registered collectors and populates
	 * {@link MetricsResponseJSON} object.
	 * 
	 * @return {@link MetricsResponseJSON}
	 */
	@SuppressWarnings("unchecked")
	public MetricsResponseJSON getMetrics() {
		boolean error = false;
		// initialized response POJO
		MetricsResponseJSON responseJSON = new MetricsResponseJSON();

		// if no collectors are registered, do nothing else get metrics from
		// collectors
		if (getMetricsCollectorsMap() == null
				|| getMetricsCollectorsMap().isEmpty()) {
			vlogDebug("No collector(s) configured. So returning...");
		} else {
			try {
				List<ComponentJSON> components = new ArrayList<ComponentJSON>();
				List<ComponentJSON> cltrCompJSON = new ArrayList<ComponentJSON>();

				Set<String> collectorKeys = getMetricsCollectorsMap()
						.keySet();
				for (String collectorKey : collectorKeys) {
					cltrCompJSON = ((MetricsCollector) getMetricsCollectorsMap()
							.get(collectorKey)).getMetrics();
					if (cltrCompJSON == null || cltrCompJSON.isEmpty()) {
						vlogDebug("No metrics returned by Collector: {0}",
								collectorKey);
					} else {
						vlogDebug("Adding metrics returned by collector: {0}",
								collectorKey);
						if(isAllowMetricAliasing()){
							createAliases(cltrCompJSON);
						}
						components.addAll(cltrCompJSON);
					}
				}
				responseJSON.setComponents(components);
			} catch (Exception e) {
				error = true;
				processError(e, responseJSON);
			} finally {
				if (!error) {
					processSuccess(responseJSON);
				}
			}
		}
		return responseJSON;
	}
	
	private Map metricAliases;
	
	public Map getMetricAliases() {
		return metricAliases;
	}

	public void setMetricAliases(Map metricAliases) {
		this.metricAliases = metricAliases;
	}

	private void createAliases(List<ComponentJSON> cltrCompJSONs) {
		List<ComponentJSON> aliasCltrCompJSONs = new ArrayList<ComponentJSON>();
		for(ComponentJSON cltrCompJSON:cltrCompJSONs){
			if(metricAliases.containsKey(cltrCompJSON.getName())){
				List<MetricJSON> metricJSONs = cltrCompJSON.getMetrics();
				ComponentJSON aliasComponentJSON = new ComponentJSON((String)metricAliases.get(cltrCompJSON.getName()),cltrCompJSON.getDescription(),metricJSONs);
				aliasCltrCompJSONs.add(aliasComponentJSON);
			}
		}
		cltrCompJSONs.addAll(aliasCltrCompJSONs);
	}

	protected void processSuccess(MetricsResponseJSON responseJSON) {
		vlogDebug("Processing success response.");
		responseJSON.setSuccess(Boolean.TRUE);
		responseJSON.setStatus(200);
		responseJSON.setMessage("OK");
	}

	protected void processError(Exception e, MetricsResponseJSON responseJSON) {
		vlogDebug("Processing error response.");
		responseJSON.setSuccess(Boolean.FALSE);
		responseJSON.setStatus(500);
		responseJSON.setMessage(e.getMessage());
	}

	/**
	 * @return the metricsCollectorsMap
	 */
	public ServiceMap getMetricsCollectorsMap() {
		return metricsCollectorsMap;
	}

	/**
	 * @param metricsCollectorsMap the metricsCollectorsMap to set
	 */
	public void setMetricsCollectorsMap(ServiceMap metricsCollectorsMap) {
		this.metricsCollectorsMap = metricsCollectorsMap;
	}
	
	/**
	 * @return the configuration
	 */
	public NewRelicApplicationConfiguration getConfiguration() {
		if(configuration == null){
			configuration = (NewRelicApplicationConfiguration) Nucleus.getGlobalNucleus().resolveName("/oe/newrelic/configuration/NewRelicApplicationConfiguration");
		}
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(NewRelicApplicationConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public boolean isAllowMetricAliasing() {
		return allowMetricAliasing;
	}

	public void setAllowMetricAliasing(boolean allowMetricAliasing) {
		this.allowMetricAliasing = allowMetricAliasing;
	}	
	
}
