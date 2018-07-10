package com.fr.design.gui.date;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fr.design.gui.ilable.UILabel;

import com.fr.general.Inter;

public class UIDayLabel extends UILabel {

    private Date date = null;

    /**
     * 日期格式（TODAY/TIP用）
     */
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * 日格式
     */
    final SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public UIDayLabel(Date date) {
        this(date, true);
    }

    public UIDayLabel(Date date, boolean isSmallLabel) {
        setHorizontalAlignment(UILabel.CENTER);
        setFont(new Font(Inter.getLocText("Song_TypeFace"), 0, 12));
        this.date = date;
        setPreferredSize(new Dimension(30, 18));
        if (isSmallLabel) {
            setText(dayFormat.format(date));
        } else {
            setText(Inter.getLocText("Today")+":" + dateFormat.format(new Date()));
        }
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}