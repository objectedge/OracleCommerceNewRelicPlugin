/**
 * 
 */
package com.objectedge.evaluator;


/**
 * <b>MetricEvaluator</b>
 * <p>Responsible to evaluate an expression.</p>
 * <ul>
 * <li><b>evaluate</b>: evaluates an expression.</b></li>
 * <li><b>getValue</b>: get the value for passed operand through argument.</b></li>
 * </ul>
 * 
 * @author Chandan Kushwaha
 *
 */
public interface MetricEvaluator {
	public Double evaluate(String expression) throws Exception;
	public Double getValue(String operand);
}
