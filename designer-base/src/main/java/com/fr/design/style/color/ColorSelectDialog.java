package com.fr.design.style.color;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.JPanel;
import java.io.Serializable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 颜色选择器更多颜色对话框
 * @author focus
 *
 */
public class ColorSelectDialog extends MiddleChartDialog{

	private Color color;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private ColorSelectDetailPane pane;
	
	private UIButton ok;
	private UIButton cancel;
	
	// 外层颜色选择器面板
	private ColorSelectable seletePane;
	
	// 颜色选择监听
	ColorTracker okListener;

	/**
	 * construct
	 * @param owner 父容器
	 */
    public ColorSelectDialog(Frame owner) {
        super(owner);
        initComponent();
    }

    /**
     * construct 
     * @param owner 父容器
     */
    public ColorSelectDialog(Dialog owner) {
        super(owner);
        initComponent();
    }
    
    /**
     * construct
     * @param owner 父容器
     * @param pane 颜色选择器更多颜色面板
     * @param initialColor 初始颜色
     * @param okListener 颜色选择监听
     * @param seletePane 外层颜色选择面板
     */
    public ColorSelectDialog(Frame owner,ColorSelectDetailPane pane,Color initialColor,ActionListener okListener,ColorSelectable seletePane){
    	super(owner);
    	this.pane = pane;
    	this.color = initialColor;
    	this.okListener = (ColorTracker) okListener;
    	this.seletePane = seletePane;
    	initComponent();
    }
	
    private void initComponent() {
    	this.setLayout(new BorderLayout());
    	this.add(pane,BorderLayout.NORTH);
    	this.setBasicDialogSize(545,500);

		this.setResizable(false);
    	
    	JPanel buttonPane = new JPanel();
    	buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	
    	this.add(buttonPane, BorderLayout.SOUTH);
    	
    	ok = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_OK"));
    	cancel = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Button_Cancel"));
    	
    	buttonPane.add(ok);
    	buttonPane.add(cancel);
    	
    	ok.setActionCommand("OK");
    	ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seletePane.setColor(okListener.getColor());
				doOK();
			}
		});
    	
    	ok.addActionListener(okListener);
    	
    	cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});

		this.setTitle(pane.title4PopupWindow());
    	GUICoreUtils.setWindowCenter(getOwner(), this);
    }

	@Override
	public BaseChartCollection getChartCollection() {
		return null;
	}

	@Override
	public void populate(BaseChartCollection cc) {
		
	}

	/**
	 * 显示颜色选择器更多颜色对话框
	 * @param owner 父容器
	 * @param pane 更多颜色选择器面板
	 * @param initialColor 初始颜色
	 * @param selectePane 外层颜色选择器面板
	 * void
	 */
	public static void showDialog(Frame owner,ColorSelectDetailPane pane,Color initialColor,ColorSelectable selectePane){
		showDialog(owner,pane,initialColor,selectePane,false);
	}

	public static void showDialog(Frame owner,ColorSelectDetailPane pane,Color initialColor,ColorSelectable selectePane, boolean alwaysOnTop){
		ColorTracker okListener = new ColorTracker(pane);
		ColorSelectDialog dialog = new ColorSelectDialog(owner,pane,initialColor,okListener,selectePane);
		dialog.setAlwaysOnTop(alwaysOnTop);
		dialog.setModal(true);
		dialog.show();
	}

	/**
	 * 不处理
	 */
	@Override
	public void checkValid() throws Exception {
		
	}

}

/**
 * 颜色选择器监听
 * @author focus
 *
 */
class ColorTracker implements ActionListener, Serializable {
	ColorSelectDetailPane chooser;
    Color color;
    
    public void setColor(Color color){
    	this.color = color;
    }

    public ColorTracker(ColorSelectDetailPane c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        color = chooser.getSelectedPanel().getSelectionModel().getSelectedColor();
    }

    public Color getColor() {
        return color;
    }
}