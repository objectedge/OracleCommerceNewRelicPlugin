package com.objectedge.newrelic.collector;

import java.util.List;

import com.objectedge.newrelic.json.ComponentJSON;
/**
 * 
 * Implementations interested in collecting metrics to be sent to NewRelic. 
 * <ul>
 * <li><b>getMetricName()</b>: must return the name of the Metric under which all data being reported will be categorized</li>
 * <li><b>getMetrics()</b>: will return the metrics that the collector needs to send upstream</li>
 * </ul>
 * 
 * @author Anand Raju
 */
public interface MetricsCollector {
	public String getMetricName();
	public List<ComponentJSON> getMetrics() throws Exception;
}
