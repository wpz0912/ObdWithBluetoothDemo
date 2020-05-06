/*
 * TODO put header 
 */
package com.wpz.obddemo.obd.reader;


import com.wpz.obddemo.obd.reader.io.ObdCommandJob;

/**
 * TODO put description
 */
public interface IPostMonitor {
	void setListener(IPostListener callback);

	boolean isRunning();

	void executeQueue();
	
	void addJobToQueue(ObdCommandJob job);
}