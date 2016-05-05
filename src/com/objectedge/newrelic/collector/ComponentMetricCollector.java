package com.objectedge.newrelic.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import atg.nucleus.GenericService;

import com.objectedge.evaluator.ComponentMetricEvaluator;
import com.objectedge.newrelic.json.ComponentJSON;
import com.objectedge.newrelic.json.MetricJSON;

/**
 * <b>ComponentCollector</b>
 * <p>Responsible for collecting component related metrics.</p>
 * 
 * @author Chandan Kushwaha
 *
 */
public class ComponentMetricCollector extends GenericService implements MetricsCollector {
	// map of metric name and its expression
	private Map<String, String> metricsExpr;
	// component metrics expression evaluator
	private ComponentMetricEvaluator evaluator;
	
	// this is a test method
	public void testGetMetric() throws Exception {
		getMetrics();
	}
	
	/* (non-Javadoc)
	 * @see com.objectedge.newrelic.collector.MetricsCollector#getMetrics(atg.servlet.DynamoHttpServletRequest, atg.servlet.DynamoHttpServletResponse)
	 */
	@Override
	public List<ComponentJSON> getMetrics() throws Exception {
		List<ComponentJSON> componentJSONs = new ArrayList<ComponentJSON>();
		
		// if no metrics expressions are found,
		// do nothing else evaluate metrics expression
		if (getMetricsExpr() == null || getMetricsExpr().isEmpty()) {
			vlogInfo("No metrics are found. So skipping......");
		} else {
			try {
				Set<String> metricKeys = (Set<String>) getMetricsExpr().keySet();
				
				String componentPath = null; // component path
				String expression = null; // expression to evaluate
				Double value = null; // value
				String[] splitStr = null;
				for (String metricKey : metricKeys) {
					expression = getMetricsExpr().get(metricKey);
					componentPath = getComponentPath(expression);
					value = getEvaluator().evaluate(expression); // evaluate expression
					splitStr = metricKey.split(":");
					addComponent(componentJSONs, componentPath, splitStr[0], splitStr[1], value);
				}
			} catch (Exception e) {
				vlogError(e, "");
			}
		}
		return componentJSONs;
	}

	/*
	 * Add or update component to component list.
	 * 
	 * @param componentList
	 * @param componentPath
	 * @param metricName
	 * @param metricUnit
	 * @param value
	 */
	private void addComponent(List<ComponentJSON> componentList,
			String componentPath, String metricName, String metricUnit, Double value) {
		ComponentJSON componentJSON = getComponent(componentList, componentPath);
		MetricJSON metricJSON = new MetricJSON(metricName, metricUnit, value);
		componentJSON.addMetric(metricJSON);
		if(!componentList.contains(componentJSON))
			componentList.add(componentJSON);
	}

	/*
	 * Returns component object
	 * 
	 * @param componentPath
	 * @return ComponentJSON
	 */
	private ComponentJSON getComponent(List<ComponentJSON> componentList, String componentPath) {
		ComponentJSON component = null;
		for (ComponentJSON componentJSON : componentList) {
			if(componentJSON.getName().equals(componentPath)) {
				component = componentJSON;
				break;
			}
		}
		
		return component == null ? new ComponentJSON(componentPath) : component;
	}

	/*
	 * @param expression
	 * @return
	 */
	private String getComponentPath(String expression) {
		String regex = "([/]\\w+)*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(expression);
		matcher.find();
		return matcher.group();
	}

	/**
	 * @return the metricsExpr
	 */
	public Map<String, String> getMetricsExpr() {
		return metricsExpr;
	}

	/**
	 * @param metricsExpr the metricsExpr to set
	 */
	public void setMetricsExpr(Map<String, String> metricsExpr) {
		this.metricsExpr = metricsExpr;
	}

	/**
	 * @return the evaluator
	 */
	public ComponentMetricEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * @param evaluator the evaluator to set
	 */
	public void setEvaluator(ComponentMetricEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/* (non-Javadoc)
	 * @see com.objectedge.newrelic.collector.MetricsCollector#getMetricName()
	 */
	@Override
	public String getMetricName() {
		// it is not used
		return null;
	}
}

