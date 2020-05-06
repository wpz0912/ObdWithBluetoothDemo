/*
 * TODO put header 
 */
package com.wpz.obddemo.obd.reader;


import com.wpz.obddemo.obd.reader.io.ObdCommandJob;

/**
 * TODO put description
 */
public interface IPostListener {

	void stateUpdate(ObdCommandJob job);
	
}