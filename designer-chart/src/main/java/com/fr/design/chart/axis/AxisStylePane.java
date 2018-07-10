package com.fr.design.chart.axis;

import com.fr.general.Inter;
import com.fr.design.dialog.BasicPane;

public abstract class AxisStylePane<T> extends BasicPane {
	private static final long serialVersionUID = 8969849654720197617L;

	public abstract void populate(T axis);
	
	public abstract void update(T axis);

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Set", "ChartF-Axis", "Format"});
	}
}