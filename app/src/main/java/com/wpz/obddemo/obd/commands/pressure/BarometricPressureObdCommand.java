/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.pressure;


import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * 大气压
 * Barometric pressure.
 */
public class BarometricPressureObdCommand extends PressureObdCommand {

	/**
	 * @param cmd
	 */
	public BarometricPressureObdCommand() {
		super("01 33");
	}

	/**
	 * @param other
	 */
	public BarometricPressureObdCommand(PressureObdCommand other) {
		super(other);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see eu.lighthouselabs.MyCommand.commands.ObdCommand#getName()
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.BAROMETRIC_PRESSURE.getValue();
	}

}