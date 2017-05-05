package ca.concordia.cse.gipsy.dev;

import java.util.Vector;
import de.vs.unikassel.generator.converter.wsdl_creator.ServiceDescription;

public class GIPSYService {

	/**
	 * The name of the service.
	 */
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * The input-instances/concepts of the service.
	 */
	
	private Vector<String> inputs;

	public Vector<String> getInputs() {
		return inputs;
	}

	public void setInputs(Vector<String> inputs) {
		this.inputs = inputs;
	}
	
	/**
	 * The output- instances/concepts of the service.
	 */
	
	private Vector<String> outputs;

	public Vector<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(Vector<String> outputs) {
		this.outputs = outputs;
	}
	
	/**
	 * The constraints of the service.
	 */
	
	private Vector<String> constraints;

	public Vector<String> getConstraints() {
		constraints.add(String.valueOf(Math.random() <0.5));
		return constraints;
	}

	public void setConstraints(Vector<String> constraints) {
		this.constraints = constraints;
	}
	
	/**
	 * The effects of the service.
	 */
	
	private Vector<String> effects;

	public Vector<String> getEffects() {
		return effects;
	}

	public void setEffects(Vector<String> effects) {
		this.effects = effects;
	}

	public GIPSYService() {
		
		this.name = null;
		this.inputs = new Vector<String>();
		this.outputs = new Vector<String>();
		this.effects = new Vector<String>();
		this.constraints = new Vector<String>();
	}
	
}