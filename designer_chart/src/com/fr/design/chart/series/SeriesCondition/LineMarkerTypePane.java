package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.general.Inter;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class LineMarkerTypePane extends ConditionAttrSingleConditionPane<DataSeriesCondition> {

    private UILabel nameLabel;
    private MarkerComboBox markerBox;

    private AttrMarkerType markerType = new AttrMarkerType();

    public LineMarkerTypePane(ConditionAttributesPane conditionAttributesPane) {
        this(conditionAttributesPane, true);
    }

    public LineMarkerTypePane(ConditionAttributesPane conditionAttributesPane, boolean isRemove) {
        super(conditionAttributesPane, isRemove);

        nameLabel = new UILabel(Inter.getLocText(new String[]{"ChartF-Marker", "FS_Report_Type"}));
        markerBox = new MarkerComboBox(MarkerFactory.getMarkerArray());

        if (isRemove) {
            this.add(nameLabel);
        }
        this.add(markerBox);
    }

    @Override
    public String nameForPopupMenuItem() {
        return Inter.getLocText(new String[]{"ChartF-Marker", "FS_Report_Type"});
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }

    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrMarkerType) {
            markerType = (AttrMarkerType) condition;
            markerBox.setSelectedMarker(MarkerFactory.createMarker(markerType.getMarkerType()));
        }
    }

    public DataSeriesCondition update() {
        this.markerType.setMarkerType(markerBox.getSelectedMarkder().getMarkerType());
        return this.markerType;
    }

}