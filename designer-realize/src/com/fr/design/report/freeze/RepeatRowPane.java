package com.fr.design.report.freeze;

import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StringUtils;

public class RepeatRowPane extends FreezeAndRepeatPane {

    public RepeatRowPane() {
        start = new RowSpinner(1,Integer.MAX_VALUE,1,1);
        end = new RowSpinner(1,Integer.MAX_VALUE,1,1);
        super.initComponent();
    }



    @Override
    protected String title4PopupWindow() {
        return "repeatcolumn";
    }

    @Override
    public void populateBean(FT ob) {
        ((RowSpinner)start).setValue((ob.getFrom() + 1));
        ((RowSpinner)end).setValue((ob.getTo() + 1));
    }

    @Override
    public FT updateBean() {
        return new FT((int)((RowSpinner) start).getValue()-1 , (int)((RowSpinner)end).getValue()-1 );
    }

    @Override
    public String getLabeshow() {
        return StringUtils.BLANK + Inter.getLocText("RowTo") + StringUtils.BLANK;
    }

}