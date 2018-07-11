package com.fr.design.present.dict;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import com.fr.data.impl.CustomDictionary;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class CustomDictPane extends FurtherBasicBeanPane<CustomDictionary> {
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {Inter.getLocText("Actual_Value"), Inter.getLocText("Display_Value")}; // 字典列名key,value

	private CustomDictCorrelationPane correlationPane;

	public CustomDictPane() {
		initComponents();
	}

	/**
	 * init components
	 */
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		correlationPane = new CustomDictCorrelationPane(columnNames) {

			public void stopPaneEditing(ChangeEvent e) {
				fireChanged();
			}
		};
		UIScrollPane scroll = new UIScrollPane(correlationPane);
		this.add(scroll, BorderLayout.CENTER);
	}


	public void populateBean(CustomDictionary customDict) {
		if (customDict == null) {
			return;
		}
		int rowCount = customDict.size();
		List<Object[]> values = new ArrayList<Object[]>();
		for (int i = 0; i < rowCount; i++) {
			values.add(new String[]{customDict.getKey(i) == null ? StringUtils.EMPTY : customDict.getKey(i).toString(),
					customDict.getValue(i) == null ? StringUtils.EMPTY : customDict.getValue(i).toString()});
		}
		correlationPane.populateBean(values);
	}

	public CustomDictionary updateBean() {
		List<Object[]> list = correlationPane.updateBean();
		java.util.List<Object> kl = new java.util.ArrayList<Object>();
		java.util.List<Object> vl = new java.util.ArrayList<Object>();
		for (Object[] aList : list) {
			kl.add(aList[0]);
			vl.add(aList[1]);
		}

		return new CustomDictionary(kl.toArray(), vl.toArray());
	}

	@Override
	public boolean accept(Object ob) {
		return ob instanceof CustomDictionary;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Datasource-User_Defined");
	}

	@Override
	public void reset() {
		correlationPane.populateBean(new ArrayList<Object[]>());
	}
}