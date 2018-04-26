package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;
import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StableUtils;

public class FreezeWriteRowPane extends FreezeAndRepeatPane {

	public FreezeWriteRowPane() {
		start = new UILabel(Inter.getLocText(new String[]{"Frozen", "N.O."}) + " 1", SwingConstants.CENTER);
		end =  new RowSpinner(1, Integer.MAX_VALUE, 1,1);
		super.initComponent();
		this.add(new UILabel(Inter.getLocText("Row")));
	}

    /**
     *
     * @param l
     */

	@Override
	protected String title4PopupWindow() {
		return "FreezeRow";
	}

	@Override
	public void populateBean(FT ob) {
		((UILabel)start).setText(Inter.getLocText(new String[]{"Frozen", "N.O."}) + String.valueOf(ob.getFrom()));
		((RowSpinner)end).setValue(ob.getTo() + 1);
	}

	@Override
	public FT updateBean() {
        return new FT(StableUtils.convertABCToInt(((UILabel) start).getText()) - 1, (int)(((RowSpinner) end).getValue() - 1));
	}

	@Override
	public String getLabeshow() {
		return Inter.getLocText("RowTo");
	}

}