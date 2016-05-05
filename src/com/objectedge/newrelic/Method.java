package com.objectedge.newrelic;

import java.util.List;

/**
 * @author Chandan Kushwaha
 *
 */
public class Method {
	private String name;
	private List<String> parameterTypes;
	
	/**
	 * 
	 */
	public Method() {
		super();
	}
	
	/**
	 * @param name
	 * @param parameterType
	 */
	public Method(String name, List<String> parameterTypes) {
		super();
		this.name = name;
		this.parameterTypes = parameterTypes;
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
	 * @return the parameterType
	 */
	public List<String> getParameterTypes() {
		return parameterTypes;
	}
	/**
	 * @param parameterType the parameterType to set
	 */
	public void setParameterTypes(List<String> parameterType) {
		this.parameterTypes = parameterType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append(" :{").
		append("name: ").append(this.name)
		.append(", parameterTypes: ").append(this.parameterTypes)
		.append("}");
		
		return sb.toString();
	}
}
