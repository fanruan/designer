package com.fr.start.server;

public interface JettyServerListener {
	/**
	 * Started
	 */
	public void started(JettyHost jettyServer);

	/**
	 * Stopped
	 */
	public void stopped(JettyHost jettyServer);
	/**
	 * Exited
	 */
	public void exited(JettyHost jettyServer);
	
}