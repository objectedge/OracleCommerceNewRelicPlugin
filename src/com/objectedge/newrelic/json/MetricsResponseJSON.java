package com.objectedge.newrelic.json;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chandan Kushwaha
 *
 */
public class MetricsResponseJSON {
	private boolean success;
	private String message;
	private int status;
	private List<ComponentJSON> components;
	
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the components
	 */
	public List<ComponentJSON> getComponents() {
		return components;
	}
	/**
	 * @param components the components to set
	 */
	public void setComponents(List<ComponentJSON> components) {
		this.components = components;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append(": {").
			append("success: ").append(this.success)
			.append(", status: ").append(this.status)
			.append(", message: ").append(this.message)
			.append(", components: ").append(this.components)
			.append("}");
		return sb.toString();
	}
	
	public void printModelJSON() {
		MetricsResponseJSON model = new MetricsResponseJSON();
		List<ComponentJSON> components = new ArrayList<ComponentJSON>();
		List<MetricJSON> metrics = new ArrayList<MetricJSON>();
		
		for (int i = 0; i < 3; i++) {
			ComponentJSON comp = new ComponentJSON();

			for (int j = 0; j < 2; j++) {
				MetricJSON metric = new MetricJSON();
				metrics.add(metric);
			}
			comp.setMetrics(metrics);
			components.add(comp);
		}
		model.setComponents(components);
		
		System.out.println(model);
	}
}
