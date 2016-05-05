package com.objectedge.newrelic.json;

/**
 * @author Chandan Kushwaha
 *
 */
public class MetricJSON {
	private String name;
	private String unit;
	private double value;
	
	/**
	 * 
	 */
	public MetricJSON() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param name
	 * @param unit
	 * @param value
	 */
	public MetricJSON(String name, String unit, double value) {
		super();
		this.name = name;
		this.unit = unit;
		this.value = value;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append(":{")
			.append("name: ").append(this.name)
			.append(", unit: ").append(this.unit)
			.append(", value: ").append(this.value)
			.append("}");
		return sb.toString();
	}
}