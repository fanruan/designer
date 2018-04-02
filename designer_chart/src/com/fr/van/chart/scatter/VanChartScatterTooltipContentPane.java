package com.fr.van.chart.scatter;

import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.scatter.attr.ScatterAttrTooltipContent;
import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.component.format.ValueFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.XFormatPaneWithCheckBox;
import com.fr.van.chart.designer.component.format.YFormatPaneWithCheckBox;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 散点图标签界面
 */
public class  VanChartScatterTooltipContentPane extends VanChartTooltipContentPane{
    private static final long serialVersionUID = 5595016643808487922L;
    private XFormatPaneWithCheckBox xFormatPane;
    private YFormatPaneWithCheckBox yFormatPane;
    private ValueFormatPaneWithCheckBox sizeFormatPane;

    public XFormatPaneWithCheckBox getxFormatPane() {
        return xFormatPane;
    }

    public YFormatPaneWithCheckBox getyFormatPane() {
        return yFormatPane;
    }

    public ValueFormatPaneWithCheckBox getSizeFormatPane() {
        return sizeFormatPane;
    }

    public VanChartScatterTooltipContentPane(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }


    @Override
    protected void initFormatPane(VanChartStylePane parent, JPanel showOnPane){
        super.initFormatPane(parent, showOnPane);
        xFormatPane = new XFormatPaneWithCheckBox(parent, showOnPane);
        yFormatPane = new YFormatPaneWithCheckBox(parent, showOnPane);
        sizeFormatPane = new ValueFormatPaneWithCheckBox(parent, showOnPane);
    }

    @Override
    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{seriesNameFormatPane,null},
                new Component[]{xFormatPane,null},
                new Component[]{yFormatPane,null},
                new Component[]{sizeFormatPane,null},
        };
    }

    @Override
    protected void populateFormatPane(AttrTooltipContent attrTooltipContent) {
        super.populateFormatPane(attrTooltipContent);
        if(attrTooltipContent instanceof ScatterAttrTooltipContent) {
            ScatterAttrTooltipContent scatterAttrTooltipContent = (ScatterAttrTooltipContent) attrTooltipContent;
            xFormatPane.populate(scatterAttrTooltipContent.getXFormat());
            yFormatPane.populate(scatterAttrTooltipContent.getYFormat());
            sizeFormatPane.populate(scatterAttrTooltipContent.getSizeFormat());
        }
    }

    @Override
    protected void updateFormatPane(AttrTooltipContent attrTooltipContent) {
        super.updateFormatPane(attrTooltipContent);
        if(attrTooltipContent instanceof ScatterAttrTooltipContent) {
            ScatterAttrTooltipContent scatterAttrTooltipContent = (ScatterAttrTooltipContent) attrTooltipContent;
            xFormatPane.update(scatterAttrTooltipContent.getXFormat());
            yFormatPane.update(scatterAttrTooltipContent.getYFormat());
            sizeFormatPane.update(scatterAttrTooltipContent.getSizeFormat());
        }
    }

    @Override
    public void setDirty(boolean isDirty) {
        xFormatPane.setDirty(isDirty);
        yFormatPane.setDirty(isDirty);
        sizeFormatPane.setDirty(isDirty);
        seriesNameFormatPane.setDirty(isDirty);
    }

    @Override
    public boolean isDirty() {
        return xFormatPane.isDirty() || yFormatPane.isDirty() || sizeFormatPane.isDirty() || seriesNameFormatPane.isDirty();
    }

    @Override
    protected AttrTooltipContent createAttrTooltip() {
        return new ScatterAttrTooltipContent();
    }
}
