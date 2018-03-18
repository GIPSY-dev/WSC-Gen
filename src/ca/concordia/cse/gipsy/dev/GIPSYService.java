package ca.concordia.cse.gipsy.dev;

import java.util.Vector;
import de.vs.unikassel.generator.converter.wsdl_creator.ServiceDescription;

public class GIPSYService extends ServiceDescription {

	/**
	 * The name of the service.a
	 */

	private String name;

	/**
	 * The input-instances/concepts of the service.
	 */
	private Vector<String> inputs;

	/**
	 * The output- instances/concepts of the service.
	 */
	private Vector<String> outputs;

	/**
	 * The constraints of the service.
	 */
	private Vector<Boolean> constraints; //Use IRandomizer here!!

	/**
	 * The effects of the service.
	 */
	private Vector<String> effects;
	
	public GIPSYService() {
		
		this.name = null;
		this.inputs = new Vector<String>();
		this.outputs = new Vector<String>();
		this.effects = new Vector<String>();
		this.constraints = new Vector<Boolean>();
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<String> getInputs() {
		return inputs;
	}

	public void setInputs(Vector<String> inputs) {
		this.inputs = inputs;
	}

	public Vector<String> getOutputs() {
		return outputs;
	}

	public void setOutputs(Vector<String> outputs) {
		this.outputs = outputs;
	}

	public Vector<Boolean> Constraints() {
		return constraints;
	}

	public void setConstraints(Vector<Boolean> constraints) {
		this.constraints = constraints;
	}

	public Vector<String> getEffects() {
		return effects;
	}

	public void setEffects(Vector<String> effects) {
		this.effects = effects;
	}

}
