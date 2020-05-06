/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.control;


import com.wpz.obddemo.obd.commands.PercentageObdCommand;
import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * TODO put description
 * 
 * Timing Advance
 */
public class TimingAdvanceObdCommand extends PercentageObdCommand {

	public TimingAdvanceObdCommand() {
		super("01 0E");
	}

	public TimingAdvanceObdCommand(TimingAdvanceObdCommand other) {
		super(other);
	}

	@Override
	public String getName() {
		return AvailableCommandNames.TIMING_ADVANCE.getValue();
	}
}