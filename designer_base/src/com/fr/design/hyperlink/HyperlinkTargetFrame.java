package com.fr.design.hyperlink;

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

