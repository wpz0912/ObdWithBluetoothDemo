/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.temperature;


import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 *环境空气温度 -40 215
 * Ambient Air Temperature. 
 */
public class AmbientAirTemperatureObdCommand extends TemperatureObdCommand {

	/**
	 * @param cmd
	 */
	public AmbientAirTemperatureObdCommand() {
		super("01 46");
	}

	/**
	 * @param other
	 */
	public AmbientAirTemperatureObdCommand(TemperatureObdCommand other) {
		super(other);
	}

	@Override
    public String getName() {
		return AvailableCommandNames.AMBIENT_AIR_TEMP.getValue();
    }

}