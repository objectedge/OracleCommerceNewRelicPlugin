package com.objectedge.newrelic;

import java.util.List;

/**
 * @author Chandan Kushwaha
 *
 */
public class Component {
	private String name;
	private List<Method> methods;
	
	/**
	 * default constructor
	 */
	public Component() {
		super();
	}	
	
	/**
	 * @param name
	 * @param methods
	 * @param metrics
	 */
	public Component(String name, List<Method> methods) {
		super();
		this.name = name;
		this.methods = methods;
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
	 * @return the methods
	 */
	public List<Method> getMethods() {
		return methods;
	}
	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Component compObj = (Component) obj;
		return this.getName().equals(compObj.getName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()).append(" :{").
		append("name: ").append(this.name)
		.append(", methods: ").append(this.methods)
		.append("}");
		
		return sb.toString();
	}

	/**
	 * @param methodName
	 * @return
	 */
	public Method getMethod(String methodName) {
		if(this.getMethods() != null && !this.getMethods().isEmpty()) {
			for (Method method : this.getMethods()) {
				if(method.getName().equals(methodName)) {
					return method;
				}
			}
		}
		return null;
	}
}
