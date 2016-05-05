package com.objectedge.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.beanutils.MethodUtils;

import atg.nucleus.GenericService;
import atg.nucleus.Nucleus;

import com.objectedge.monitoring.registry.ComponentMonitoringRegistry;
import com.objectedge.newrelic.Component;

/**
 * <b>ComponentMetricEvaluator</b>
 * <p>
 * It evaluates component metric expressions. Expression format is as below:
 * </p>
 * <code><metric-name>:<unit>=<component>.<method1><operator><component>.<method2></code>
 * <p>Here,</p> 
 * <p>metric-name and unit are separated by colon(:) and forms an unique key.</p>
 * <p>operator is optional</p>
 * <p>component and method combination invokes component's method and always returns double value.</p>
 * 
 * 
 * @author Chandan Kushwaha
 *
 */
public class ComponentMetricEvaluator extends GenericService implements MetricEvaluator {

	private static final String PERAND_REGEX = "(([/]\\w+)*)[.]\\w+[.\\w]+([(]{1}([\\w]+[:]*[\\w\\d.])*[)]{1})";
	private ComponentMonitoringRegistry componentMonitoringRegistry;
	
	/* (non-Javadoc)
	 * @see com.objectedge.evaluator.MetricEvaluator#evaulate(java.lang.String)
	 */
	@Override
	public Double evaluate(String expression) throws Exception {
		String operandRegex = PERAND_REGEX;
		Pattern pattern = Pattern.compile(operandRegex);
		Matcher matcher = pattern.matcher(expression);
		
		Double value = 0.0d;
		String matchedString = null;
		while (matcher.find()) {
			matchedString = matcher.group();
			value = getValue(matchedString);
			expression = expression.replace(matchedString, value.toString());
		}
		
		ScriptEngineManager engineMgr = new ScriptEngineManager();
		ScriptEngine engine = engineMgr.getEngineByName("javascript");
		value = 0.0d;
		Object result = engine.eval(expression);
		
		if(result != null) {
			value = Double.parseDouble(result.toString());
		}
		
		return value;
	}

	/* (non-Javadoc)
	 * @see com.objectedge.evaluator.MetricEvaluator#getValue(java.lang.String)
	 */
	@Override
	public Double getValue(String operand) {
		String[] splitStr = operand.split("[()]");
		
		String regex = "[/\\w+]+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(splitStr[0]);
		
		matcher.find();
		String componentPath = matcher.group();
		
		matcher.find();
		String methodName = matcher.group();
		
		List<Object> argumentValues = new ArrayList<Object>();
		splitStr = splitStr[1].split(":");
		for (int i = 0; i < splitStr.length; i++) {
			argumentValues.add(splitStr[i]);
		}
		
		getComponentMonitoringRegistry().addItemToMonitor(componentPath);
		Component component = getComponentMonitoringRegistry().getComponent(componentPath);
		
		if(component.getMethod(methodName) != null) {
			try {
				Object value = invokeMethod(component, methodName, getArguments(component, methodName, argumentValues));
				if(value != null) {
					return Double.parseDouble(value.toString());
				}
			} catch (Exception e) {
				vlogError(e, "Error while invoking component: {0} method: {1}", componentPath, methodName);
			}
		}
		
		return null;
	}

	/*
	 * Get arguments
	 * 
	 * @param component
	 * @param methodName
	 * @param argumentValues
	 * @return
	 */
	private Object[] getArguments(Component component, String methodName,
			List<Object> argumentValues) {
		List<String> parameterTypes = component.getMethod(methodName).getParameterTypes();
		Object[] arguments = null;
		if(parameterTypes == null || parameterTypes.isEmpty() || argumentValues == null || argumentValues.isEmpty() || parameterTypes.size() != argumentValues.size()) {
			vlogDebug("Mismatch between parameters and its values");
		} else {
			arguments = new Object[parameterTypes.size()];
			for (int i = 0; i < parameterTypes.size(); i++) {
				if(parameterTypes.get(i).equalsIgnoreCase("java.lang.String")) {
					arguments[i] = new String(argumentValues.get(i).toString());
				} else if(parameterTypes.get(i).equalsIgnoreCase("java.lang.Boolean")) {
					arguments[i] = new Boolean(argumentValues.get(i).toString());
				} else if(parameterTypes.get(i).equalsIgnoreCase("java.lang.Integer")) {
					arguments[i] = new Integer(argumentValues.get(i).toString());
				} else if(parameterTypes.get(i).equalsIgnoreCase("java.lang.Double")) {
					arguments[i] = new Double(argumentValues.get(i).toString());
				} else if(parameterTypes.get(i).equalsIgnoreCase("java.lang.Float")) {
					arguments[i] = new Float(argumentValues.get(i).toString());
				} else if(parameterTypes.get(i).equalsIgnoreCase("java.lang.Long")) {
					arguments[i] = new Long(argumentValues.get(i).toString());
				}
			}
		}
		return arguments;
	}

	/*
	 * Invoke component's method using reflection.
	 * 
	 * @param component
	 * @param methodName
	 * @param arguments
	 * @return
	 * @throws Exception
	 */
	private Object invokeMethod(Component component, String methodName, Object[] arguments) throws Exception {
		Object object = Nucleus.getGlobalNucleus().resolveName(component.getName());
		return MethodUtils.invokeMethod(object, methodName, arguments);
	}

	/**
	 * @return the componentMonitoringRegistry
	 */
	public ComponentMonitoringRegistry getComponentMonitoringRegistry() {
		return componentMonitoringRegistry;
	}

	/**
	 * @param componentMonitoringRegistry the componentMonitoringRegistry to set
	 */
	public void setComponentMonitoringRegistry(
			ComponentMonitoringRegistry componentMonitoringRegistry) {
		this.componentMonitoringRegistry = componentMonitoringRegistry;
	}
}
