package com.objectedge.newrelic.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Chandan Kushwaha
 *
 */
public class ComponentJSON {
	private String name;
	private String description;
	private List<MetricJSON> metrics;
	private String GUID = "com.objectedge.oracle.atg";
	private Date lastSuccessfulReportedAt;
	/**
	 * 
	 */
	public ComponentJSON() {
		super();
	}
	
	/**
	 * @param name
	 * @param description
	 * @param metrics
	 */
	public ComponentJSON(String name, String description,
			List<MetricJSON> metrics) {
		super();
		this.name = name;
		this.description = description;
		this.metrics = metrics;
	}

	/**
	 * @param componentPath
	 */
	public ComponentJSON(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the metrics
	 */
	public List<MetricJSON> getMetrics() {
		if(this.metrics == null) {
			this.metrics = new ArrayList<MetricJSON>();
		}
		return this.metrics;
	}
	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(List<MetricJSON> metrics) {
		this.metrics = metrics;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append(" :{")
			.append("name: ").append(this.name)
			.append(", guid: ").append(GUID)
			.append(", duration: ").append(calculateDuration())
			.append(", description: ").append(this.description)
			.append(", metrics: ").append(this.metrics)
			.append("}");
		return sb.toString();
	}

	/**
	 * @param metricJSON
	 */
	public void addMetric(MetricJSON metric) {
		getMetrics().add(metric);
	}
	
	private int calculateDuration() {
        if (lastSuccessfulReportedAt == null) {
            return 60; // default duration
        }
        long now = new Date().getTime();
        // round up duration to whole second
        long duration = (long) Math.ceil((now - lastSuccessfulReportedAt.getTime()) / 1000.0);
        return (int) duration;
    }
}