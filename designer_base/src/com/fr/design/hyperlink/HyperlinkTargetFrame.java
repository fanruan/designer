package com.fr.design.hyperlink;

import com.fr.general.ComparatorUtils;

/**
 * Created by ibm on 2016/10/13.
 */
public enum HyperlinkTargetFrame {
    BLANK_FRAME("_blank", 0), DIALOG_FRAME("_dialog", 1), SELF_FRAME("_self", 2);

    private String name;
    private int index;
    private static HyperlinkTargetFrame[] arrayOfValues;

    HyperlinkTargetFrame(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static HyperlinkTargetFrame parse(int index) {
        if (arrayOfValues == null) {
            arrayOfValues = HyperlinkTargetFrame.values();
        }
        for (HyperlinkTargetFrame hyperlinkTargetFrame : HyperlinkTargetFrame.values()) {
            if (hyperlinkTargetFrame.getIndex() == index) {
                return hyperlinkTargetFrame;
            }
        }
        return BLANK_FRAME;
    }

    public static int convert(String name) {
        if (arrayOfValues == null) {
            arrayOfValues = HyperlinkTargetFrame.values();
        }
        for (HyperlinkTargetFrame hyperlinkTargetFrame : HyperlinkTargetFrame.values()) {
            if (ComparatorUtils.equals(hyperlinkTargetFrame.getName(), name)) {
                return hyperlinkTargetFrame.getIndex();
            }
        }
        return BLANK_FRAME.getIndex();
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }
}

