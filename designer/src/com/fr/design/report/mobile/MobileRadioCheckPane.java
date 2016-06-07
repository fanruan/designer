package com.fr.design.report.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MobileRadioCheckPane extends BasicBeanPane<Boolean> {

	private List<UICheckBox> checkBoxes = new ArrayList<UICheckBox>();

	public MobileRadioCheckPane(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p};
		double[] columnSize = {p,p};

		UICheckBox checkBox = new UICheckBox(Inter.getLocText("FS-CPT_ZOOM"));
		checkBox.setSelected(true);
		
		checkBoxes.add(checkBox);

		Component[][] components = new Component[][]{
                new Component[]{new UILabel(title), checkBox}
        };
        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));

        this.add(fitOpsPane);
	}

	public int getCurrentState() {
		return checkBoxes.get(0).isSelected() ? 0 : 1;
	}
	/**
     * 设置按钮状态
     */
    public void setEnabled(boolean enabled) {
        for (UICheckBox checkBox : checkBoxes) {
            checkBox.setEnabled(enabled);
        }
    }

	@Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

	@Override
	public void populateBean(Boolean ob) {
		checkBoxes.get(0).setSelected(ob);
	}

	@Override
	public Boolean updateBean() {
		int state = getCurrentState();
		return state == 0 ? true : false;
	}
}