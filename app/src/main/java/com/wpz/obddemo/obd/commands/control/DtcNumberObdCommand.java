/*
 * TODO put header
 */
package com.wpz.obddemo.obd.commands.control;


import com.wpz.obddemo.obd.commands.ObdCommand;
import com.wpz.obddemo.obd.enums.AvailableCommandNames;

/**
 * This command will for now read MIL (check engine light) state and number of
 * diagnostic trouble codes currently flagged in the ECU.
 * 
 * Perhaps in the future we'll extend this to read the 3rd, 4th and 5th bytes of
 * the response in order to store information about the availability and
 * completeness of certain on-board tests.
 */
public class DtcNumberObdCommand extends ObdCommand {

	private int codeCount = 0;
	private boolean milOn = false;

	/**
	 * Default ctor.
	 */
	public DtcNumberObdCommand() {
		super("01 01");
	}

	/**
	 * Copy ctor.
	 * 
	 * @param other
	 */
	public DtcNumberObdCommand(DtcNumberObdCommand other) {
		super(other);
	}

	/**
	 * 
	 */
	public String getFormattedResult() {
		String res = getResult();

		if (!"NODATA".equals(res)) {
			// ignore first two bytes [hh hh] of the response
			int mil = buffer.get(2);
			if ((mil & 0x80) == 128)
				milOn = true;

			codeCount = mil & 0x7F;
		}

		res = milOn ? "MIL is ON" : "MIL is OFF";

		return new StringBuilder().append(res).append(codeCount)
				.append(" codes").toString();
	}

	/**
	 * @return the number of trouble codes currently flaggd in the ECU.
	 */
	public int getTotalAvailableCodes() {
		return codeCount;
	}

	/**
	 * 
	 * @return the state of the check engine light state.
	 */
	public boolean getMilOn() {
		return milOn;
	}

	@Override
	public String getName() {
		return AvailableCommandNames.DTC_NUMBER.getValue();
	}

}