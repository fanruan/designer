package com.fr.design.report.freeze;

import java.awt.Dimension;
import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.event.ChangeListener;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.FT;

/**
 * 重复与冻结
 * 
 * @author Daniel~
 * 
 */
public abstract class FreezeAndRepeatPane extends BasicBeanPane<FT> {
	protected JComponent start;
	protected JComponent end;

	protected UILabel connectionLabel;

	protected boolean isEnalbed;

	protected void initComponent() {
		Dimension size = new Dimension(43, 21);
        if (start instanceof UISpinner) {
            start.setPreferredSize(size);
        }
        if (end instanceof UISpinner) {
            end.setPreferredSize(size);
        }
		this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());
		this.add(start);
		connectionLabel = new UILabel(getLabeshow());
		this.add(connectionLabel);
		this.add(end);
	}

	public abstract String getLabeshow();

	@Override
	public void setEnabled(boolean flag) {
		this.isEnalbed = flag;
		if (start instanceof UISpinner) {
			start.setEnabled(flag);
		}
		if (end instanceof UISpinner) {
            end.setEnabled(flag);
		}
	}

	@Override
/**
 *  返回组件是否可用
 */
	public boolean isEnabled() {
		return this.isEnalbed;
	}

/**
 * 给UISpinner添加Listener
 */
	public void addListener(ChangeListener l) {
		if (end instanceof UISpinner) {
			((UISpinner) end).addChangeListener(l);
		}
	}

}