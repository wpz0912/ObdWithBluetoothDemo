/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.engine;


import com.wpz.obddemo.obd.commands.ObdCommand;
import com.wpz.obddemo.obd.commands.PercentageObdCommand;
import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * Calculated Engine Load value.
 */
public class EngineLoadObdCommand extends PercentageObdCommand {

	/**
	 * @param command
	 */
	public EngineLoadObdCommand() {
		super("01 04");
	}

	/**
	 * @param other
	 */
	public EngineLoadObdCommand(ObdCommand other) {
		super(other);
	}

	/* (non-Javadoc)
	 * @see eu.lighthouselabs.MyCommand.commands.ObdCommand#getName()
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.ENGINE_LOAD.getValue();
	}

}