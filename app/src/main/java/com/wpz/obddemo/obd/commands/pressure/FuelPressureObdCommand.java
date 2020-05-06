package com.wpz.obddemo.obd.commands.pressure;


import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * 燃油压力计量 单位kpa
 */
public class FuelPressureObdCommand extends PressureObdCommand {

	public FuelPressureObdCommand() {
		super("010A");
	}

	public FuelPressureObdCommand(FuelPressureObdCommand other) {
		super(other);
	}

	/**
	 * TODO
	 * 
	 * put description of why we multiply by 3
	 * 
	 * @param temp
	 * @return
	 */
	@Override
	protected final int preparePressureValue() {
		return tempValue * 3;
	}

	@Override
	public String getName() {
		return AvailableCommandNames.FUEL_PRESSURE.getValue();
	}
	
}