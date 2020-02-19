package com.fr.design.chart;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.third.joda.time.DateTime;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 封装一层 图表新建的对话框, 配合属性表确定: 先单独只要一种图表类型的对话框.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-7 下午07:29:15
 */
public class ChartDialog extends MiddleChartDialog {
	
	private BaseChartCollection cc;
	
	private UIButton ok;
	private UIButton cancel;

    public ChartDialog(Frame owner) {
        super(owner);
        initComponent();
    }

    public ChartDialog(Dialog owner) {
        super(owner);
        initComponent();
    }
	
    private void initComponent() {
		final String createTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");

		this.setModal(true);
		this.setLayout(new BorderLayout());
    	final ChartTypePane chartTypePane = new ChartTypePane();
    	setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_M_Popup_Chart_Type"));

        this.applyClosingAction();
        this.applyEscapeAction();
    	this.setBasicDialogSize(BasicDialog.DEFAULT);
    	this.add(chartTypePane, BorderLayout.CENTER);
    	
    	JPanel buttonPane = new JPanel();
    	buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	
    	this.add(buttonPane, BorderLayout.SOUTH);
    	
    	ok = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK"));
    	cancel = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cancel"));
    	
    	buttonPane.add(ok);
    	buttonPane.add(cancel);
    	
    	ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chartTypePane.update((ChartCollection)cc, createTime);
				doOK();
			}
		});
    	
    	cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
    	
    	GUICoreUtils.setWindowCenter(getOwner(), this);
    }

    /**
     * 不处理
     */
	public void checkValid() throws Exception {
		
	}
	
	/**
	 * 更新新建的图表 ChartCollection
	 */
    public void populate(BaseChartCollection cc) {
        if (cc == null) {
            return;
        }
        this.cc = cc;
    }

    /**
     * 返回当前正在编辑的图表ChartCollection
     */
    public BaseChartCollection getChartCollection() {
        return this.cc;
    }
}