package com.fr.design.data.datapane;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.UIComponentUtils;


import javax.swing.*;
import java.awt.*;

public class VerticalChoosePane extends ChoosePane implements DesignerBean {
    private static final int RIGHTBORDER = 5;
    private static final int MAX_WIDTH = 60;

    public VerticalChoosePane(Previewable previewable) {
        this(previewable, -1);
    }

    public VerticalChoosePane(Previewable previewable, int labelSize) {
        super(previewable, labelSize);
        DesignerContext.setDesignerBean("databasename", this);
    }

    @Override
    protected void initComponentsLayout(PreviewLabel previewLabel, int labelSize) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};

        JPanel rs = new JPanel(new BorderLayout(0, 0));
        rs.add(tableNameComboBox, BorderLayout.CENTER);
        rs.add(GUICoreUtils.createFlowPane(new Component[]{new RefreshLabel(this), previewLabel}, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);
        rs.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, -RIGHTBORDER));
        UILabel l1 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database"), UILabel.LEFT);
        UILabel l2 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Model"), UILabel.LEFT);
        UILabel l3 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Select_Table"), UILabel.LEFT);
        UIComponentUtils.setLineWrap(l3, MAX_WIDTH);
        if (labelSize > 0) {
            Dimension pSize = new Dimension(labelSize, 20);
            l1.setPreferredSize(pSize);
            l2.setPreferredSize(pSize);
            l3.setPreferredSize(pSize);
        }

        Component[][] components = new Component[][]{
                new Component[]{l1, dsNameComboBox},
                new Component[]{l2, schemaBox},
                new Component[]{l3, rs}
        };

        JPanel content = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_HUGER, LayoutConstants.VGAP_LARGE);
        this.setLayout(new BorderLayout());
        this.add(content, BorderLayout.CENTER);
    }


    @Override
    public void refreshBeanElement() {
        initDsNameComboBox();
    }

}
