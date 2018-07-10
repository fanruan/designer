package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;
import com.fr.form.ui.container.WCardLayout;

public class CardDefaultShowRenderer extends EncoderCellRenderer {

    public CardDefaultShowRenderer(WCardLayout layout) {
        super(new CardDefaultShowWrapper(layout));
    }
}