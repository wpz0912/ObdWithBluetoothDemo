/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.protocol;


import com.wpz.obddemo.obd.commands.ObdCommand;

/**
 * Turns off line-feed.
 */
public class LineFeedOffObdCommand extends ObdCommand {

	/**
	 * @param command
	 */
	public LineFeedOffObdCommand() {
		super("AT L0");
	}

	/**
	 * @param other
	 */
	public LineFeedOffObdCommand(ObdCommand other) {
		super(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.lighthouselabs.MyCommand.commands.ObdCommand#getFormattedResult()
	 */
	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Line Feed Off";
	}

}