package com.fr.start.server;

public interface TomcatServerListener {
	/**
	 * Started
	 */
	public void started(TomcatHost tomcatServer);

	/**
	 * Stopped
	 */
	public void stopped(TomcatHost tomcatServer);
	/**
	 * Exited
	 */
	public void exited(TomcatHost tomcatServer);
	
}