/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.pressure;


import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * 进气歧管绝对压力
 * Intake Manifold Pressure
 */
public class IntakeManifoldPressureObdCommand extends PressureObdCommand {

	/**
	 * Default ctor.
	 */
	public IntakeManifoldPressureObdCommand() {
		super("01 0B");
	}

	/**
	 * Copy ctor.
	 * 
	 * @param other
	 */
	public IntakeManifoldPressureObdCommand(
			IntakeManifoldPressureObdCommand other) {
		super(other);
	}

	@Override
	public String getName() {
		return AvailableCommandNames.INTAKE_MANIFOLD_PRESSURE.getValue();
	}
	
}