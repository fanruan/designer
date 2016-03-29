package com.fr.design.mainframe.bbs;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

/**
 * 封装浏览器的dialog
 * 
 */
public class BBSBrowserDialog extends UIDialog { 
	
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
   
    private DisplayThread displayThread;  
    private Canvas canvas;
    private String url;
    
	public BBSBrowserDialog(Frame parent) {
		super(parent);

		this.displayThread = new DisplayThread();
		this.displayThread.start();
		this.canvas = new Canvas();
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		
		this.setSize(WIDTH, HEIGHT);
		GUICoreUtils.centerWindow(this);
	}
	
	@Override
	public String getTitle() {
		return Inter.getLocText("FR-Designer_Forum");
	}

	/**
	 * 通知组件可以显示了
	 * 
	 */
    public void addNotify() {  
        super.addNotify();  
        Display dis = displayThread.getDisplay();  
        dis.syncExec(new Runnable() {  
   
            public void run() {  
                Shell shell = SWT_AWT.new_Shell(displayThread.getDisplay(), canvas);  
                shell.setLayout(new FillLayout());  
                final Browser browser = new Browser(shell, SWT.NONE);  
                browser.setLayoutData(BorderLayout.CENTER);  
                browser.setUrl(url);  
            }  
        });  
    }
    
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 检测面板中内容合法性
	 * 
	 */
	public void checkValid() throws Exception {
	} 
	
	/**
	 * 在窗口中显示指定的url
	 * 
	 * @param url 指定的url
	 * 
	 */
	public void showWindow(String url){
		this.setUrl(url);
		this.setVisible(true);
	}
}