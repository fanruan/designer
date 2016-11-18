package com.fr.design.dialog.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类要被复用，移动到design_base中去
 */
public class MobileRadioCheckPane extends BasicBeanPane<Boolean> {

	private List<UICheckBox> checkBoxes = new ArrayList<UICheckBox>();

	public MobileRadioCheckPane(String title) {
		initComponents(title);
	}

	private void initComponents(String title) {
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p};
		double[] columnSize = {p,p};

		UICheckBox checkBox = new UICheckBox(title);
		checkBox.setSelected(false);
		
		checkBoxes.add(checkBox);

		Component[][] components = new Component[][]{
                new Component[]{checkBox}
        };
        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

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