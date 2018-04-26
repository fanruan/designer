package com.fr.design.condition;


import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public abstract class SingleConditionPane<T> extends BasicPane {
	private static final long serialVersionUID = -4274960170813368817L;
	
	protected UIButton cancel;
	
	public SingleConditionPane(){
		this(true);
	}
	
	public SingleConditionPane(boolean isRemove) {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		if (isRemove) {
			
			if(cancel == null) {
				cancel  = new UIButton(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
				cancel.setToolTipText(Inter.getLocText("FR-Action_Remove"));
				cancel.addActionListener(cancleListener);
				cancel.setMargin(new Insets(0, 0, 0, 0));
			}
			
			this.add(cancel);
		}
	}
	
	ActionListener cancleListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			doCancel();
		}
	};

    /**
     * 取消
     */
	public abstract void doCancel();

    public abstract void setDefault();
	
	public abstract void populate(T condition);
	
	public abstract T update();
}