package com.fr.design.expand;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
/**
 *
 * @editor zhou
 * @since 2012-3-23下午3:21:02
 */
public class ExpandDirectionPane extends JPanel {

    private UIRadioButton t2bRadioButton;
    private UIRadioButton l2rRadioButton;
    private UIRadioButton noneRadioButton;
    private String InsertText = StringUtils.BLANK;

    public ExpandDirectionPane() {
        super();
        this.initComponents();
    }

    public void initComponents() {
        this.setLayout(new GridLayout(1, 3));
    	JPanel innerthis=FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
    	this.add(innerthis);
        t2bRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Top_To_Bottom"));
        l2rRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("FIne-Design_Report_Utils_Left_To_Right"));
        noneRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Not_Expand"));
        ButtonGroup bg = new ButtonGroup();
        bg.add(t2bRadioButton);
        bg.add(l2rRadioButton);
        bg.add(noneRadioButton);
        innerthis.add(GUICoreUtils.createFlowPane(new Component[]{new UILabel(InsertText), t2bRadioButton},
            FlowLayout.LEFT));
        innerthis.add(GUICoreUtils.createFlowPane(new Component[]{new UILabel(InsertText), l2rRadioButton},
            FlowLayout.LEFT));
        innerthis.add(GUICoreUtils.createFlowPane(new Component[]{new UILabel(InsertText), noneRadioButton},
            FlowLayout.LEFT));
    }

    public void populate(CellExpandAttr cellExpandAttr) {
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
        }

        switch (cellExpandAttr.getDirection()) {
            case Constants.TOP_TO_BOTTOM:
                t2bRadioButton.setSelected(true);
                break;
            case Constants.LEFT_TO_RIGHT:
                l2rRadioButton.setSelected(true);
                break;
            default:
                noneRadioButton.setSelected(true);
        }
    }

    public void update(CellExpandAttr cellExpandAttr) {
        if (cellExpandAttr == null) {
            cellExpandAttr = new CellExpandAttr();
        }

        if (t2bRadioButton.isSelected()) {
            cellExpandAttr.setDirection(Constants.TOP_TO_BOTTOM);
        } else if (l2rRadioButton.isSelected()) {
            cellExpandAttr.setDirection(Constants.LEFT_TO_RIGHT);
        } else {
            cellExpandAttr.setDirection(Constants.NONE);
        }
    }

    public void setNoneRadioButtonSelected(boolean isSummary) {
        if (isSummary) {
            this.noneRadioButton.setSelected(true);
            this.l2rRadioButton.setEnabled(false);
            this.t2bRadioButton.setEnabled(false);
            this.noneRadioButton.setEnabled(false);
        } else {
            this.t2bRadioButton.setEnabled(true);
            this.l2rRadioButton.setEnabled(true);
            this.noneRadioButton.setEnabled(true);
            if (this.noneRadioButton.isSelected()) {
                this.t2bRadioButton.setSelected(true);
            }
        }
    }
}