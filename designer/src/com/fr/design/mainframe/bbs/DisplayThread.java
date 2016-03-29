package com.fr.design.mainframe.bbs;

import org.eclipse.swt.widgets.Display;

public class DisplayThread extends Thread {
 
    private Display display;
    Object sem = new Object();
 
    /**
	 * 运行
	 * 
	 */
    public void run() {
        synchronized (sem) {
            display = Display.getDefault();
            sem.notifyAll();
        }
        swtEventLoop();
    }
 
    private void swtEventLoop() {
        while (true) {
        	try{
	            if (!display.readAndDispatch()) {
	                display.sleep();
	            }
        	}catch (Exception e) {
        		continue;
			}
        }
    }
 
    public Display getDisplay() {
        try {
            synchronized (sem) {
                while (display == null) {
                    sem.wait();
                }
                return display;
            }
        } catch (Exception e) {
            return null;
        }
    }
}