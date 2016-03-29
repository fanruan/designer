package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by eason on 15/1/26.
 */
public class ChartDataPointLabel4GisPane extends ChartDatapointLabelPane{

    private UICheckBox isAddress;
    private UICheckBox isAddressName;
    private UICheckBox isAddressTittle;
    private UICheckBox isDatapointValue;

    public ChartDataPointLabel4GisPane(ChartStylePane parent){
        this.parent = parent;

        isLabelShow = new UICheckBox(Inter.getLocText("FR-Chart-Chart_Label"));

        isAddressTittle = new UICheckBox(Inter.getLocText("Chart-Area_Title"));
        isAddress = new UICheckBox(Inter.getLocText("Chart-Gis_Address"));
        isAddress.setSelected(true);
        isAddressName = new UICheckBox(Inter.getLocText("Chart-Address_Name"));

        isDatapointValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));

        divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        textFontPane = new ChartTextAttrPane();

        initFormatListener();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = { p, f };
        double[] rowSize = { p,p,p,p,p,p};

        JPanel delimiterPane = new JPanel(new BorderLayout(LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM));
        delimiterPane.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Delimiter_Symbol")), BorderLayout.WEST);
        delimiterPane.add(divideComoBox, BorderLayout.CENTER);

        Component[][] components = new Component[][]{
                new Component[]{isAddress, null},
                new Component[]{isAddressName, null},
                new Component[]{isAddressTittle, null},
                new Component[]{isDatapointValue, valueFormatButton},
                new Component[]{delimiterPane, null},
                new Component[]{textFontPane, null}
        };

        labelPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        double[] row = {p,p};
        double[] col = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
        JPanel panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                new Component[]{isLabelShow,null},new Component[]{null, labelPane}}, row, col);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER) ;

        isLabelShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    public void populate(DataSeriesCondition attr) {
        if(attr == null) {
            isLabelShow.setSelected(false);
        }else if (attr instanceof AttrContents) {
            AttrContents attrContents = (AttrContents) attr;
            populate(attrContents);
        }

        if(textFontPane != null) {
            if(attr != null) {
                textFontPane.populate(((AttrContents)attr).getTextAttr());
            } else {
                FRFont tmpFont = FRFont.getInstance();
                tmpFont.setForeground(Color.white);
                textFontPane.populate(tmpFont);
            }
        }

        checkBoxUse();
    }

    public void populate(AttrContents attrContents){
        isLabelShow.setSelected(true);
        String dataLabel = attrContents.getSeriesLabel();
        if (dataLabel != null) {
            for (int i = 0; i < ChartConstants.DELIMITERS.length; i++) {
                String delimiter = ChartConstants.DELIMITERS[i];
                if (divideComoBox != null && dataLabel.contains(delimiter)) {
                    divideComoBox.setSelectedItem(delimiter);
                    break;
                }
            }
            // 以前的换行符 ${BR}
            if (divideComoBox != null && dataLabel.contains(ChartConstants.BREAKLINE_PARA)) {
                divideComoBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
            }
            isAddressTittle.setSelected(dataLabel.contains(ChartConstants.AREA_TITTLE_PARA));
            isAddress.setSelected(dataLabel.contains(ChartConstants.ADDRESS_PARA));
            isAddressName.setSelected(dataLabel.contains(ChartConstants.ADDRESS_NAME_PARA));
            isDatapointValue.setSelected(dataLabel.contains(ChartConstants.VALUE_PARA));
        } else {
            isAddressTittle.setSelected(false);
            isAddress.setSelected(false);
            isAddressName.setSelected(false);
            isDatapointValue.setSelected(false);
        }
        valueFormat = attrContents.getFormat();
    }

    public AttrContents update() {
        if(!isLabelShow.isSelected()) {
            return null;
        }

        AttrContents attrContents = new AttrContents();
        String contents = StringUtils.EMPTY;
        String delString = Utils.objectToString(divideComoBox.getSelectedItem());
        if (delString.contains(ChartConstants.DELIMITERS[NEWLIEN])) {
            delString = ChartConstants.BREAKLINE_PARA;
        } else if (delString.contains(ChartConstants.DELIMITERS[SPACE])) {
            delString = StringUtils.BLANK;
        }

        if(isDatapointValue.isSelected()){
            contents += ChartConstants.VALUE_PARA + delString;
        }
        if(isAddressTittle.isSelected()){
            contents += ChartConstants.AREA_TITTLE_PARA + delString;
        }
        if(isAddress.isSelected()){
            contents += ChartConstants.ADDRESS_PARA + delString;
        }
        if(isAddressName.isSelected()){
            contents += ChartConstants.ADDRESS_NAME_PARA + delString;
        }

        attrContents.setSeriesLabel(contents);

        if(valueFormat != null) {
            attrContents.setFormat(valueFormat);
        }

        if(textFontPane != null){
            attrContents.setTextAttr(textFontPane.update());
        }

        return attrContents;
    }
}