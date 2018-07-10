/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.bar;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.accessibility.AccessibleContext;
import javax.swing.*;

import com.fr.base.DynamicUnitList;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.GridUtils;
import com.fr.report.ReportHelper;

/**
 * ScrollBar change its max value dynamically.
 */
public class DynamicScrollBar extends JScrollBar {
    private ElementCasePane reportPane;
    private boolean isSupportHide = true;//是否支持将ScrollBar隐藏起来.
    private int dpi;

    public DynamicScrollBar(int orientation, ElementCasePane reportPane, int dpi) {
        super(orientation);

        this.reportPane = reportPane;

        this.dpi = dpi;

        //init some values.h
        this.setMinimum(0);
        this.setUnitIncrement(1);
        this.setBlockIncrement(3);

        this.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                ajustValues();
            }

            public void componentMoved(ComponentEvent e) {
                ajustValues();
            }

            public void componentShown(ComponentEvent e) {
                ajustValues();
            }

            public void componentHidden(ComponentEvent e) {
                ajustValues();
            }

            private void ajustValues() {
                DynamicScrollBar.this.setValue(DynamicScrollBar.this.getValue());
            }
        });
    }

    public void setDpi(int dpi){
        this.dpi = dpi;
    }

    public ElementCasePane getReportPane() {
        return reportPane;
    }

    public void setReportPane(ElementCasePane reportPane) {
        if (reportPane == null) {
            return;
        }

        this.reportPane = reportPane;

        //ajust
        if (orientation == Adjustable.VERTICAL) {
            verticalBarHelper.setValue(this.reportPane.getGrid().getVerticalValue());
        } else {
            horizontalBarHelper.setValue(this.reportPane.getGrid().getHorizontalValue());
        }
    }

    /**
     * Returns the scrollbar's extent, aka its "visibleAmount".
     */
    @Override
    public int getVisibleAmount() {
        if (this.reportPane == null) {
            return 0;
        }

        if (orientation == Adjustable.VERTICAL) {
            return verticalBarHelper.getExtent(this.getValue());
        } else {
            return horizontalBarHelper.getExtent(this.getValue());
        }
    }


    public void updateUI() {
        setUI(new DynamicScrollBarUI());
    }

    @Override
    public void setValue(int value) {
        if (reportPane == null) {
            return;
        }

        if (orientation == Adjustable.VERTICAL) {
            verticalBarHelper.setValue(value);
        } else {
            horizontalBarHelper.setValue(value);
        }
    }



    private abstract class DynamicScrollBarHelper extends BarHelper {
        protected abstract DynamicUnitList getSizeList();

        protected abstract int getLastIndex();

        @Override
        public int getExtent(int currentValue) {
            return GridUtils.getExtentValue(currentValue, getSizeList(), getVisibleSize(), dpi) - currentValue;
        }

        private int adjustNewValue(int newValue, int oldValue, DynamicUnitList sizeList) {
            //adjust zero.
            if (newValue < 0) {
                newValue = 0;
            }

            //ajust new value, ignore these row which height is zero.
            if (oldValue < newValue) {
                int increased = newValue - oldValue;

                int tmpIdx = 0;
                for (int i = (oldValue + 1); true; i++) {
                    if (sizeList.get(i).more_than_zero()) {
                        tmpIdx++;

                        if (tmpIdx == increased) {
                            newValue = i;
                            break;
                        }
                    }
                }
            } else if (oldValue > newValue) {
                int decreased = oldValue - newValue;

                int tmpIdx = 0;
                for (int i = (oldValue - 1); i >= 0; i--) {
                    if (sizeList.get(i).more_than_zero()) {
                        tmpIdx++;

                        if (tmpIdx == decreased) {
                            newValue = i;
                            break;
                        }
                    }
                }
            }
            return newValue;
        }

        public void setValue(int newValue) {
            int oldValue = DynamicScrollBar.this.getValue();

            DynamicUnitList sizeList = getSizeList();
            int lastIndex = getLastIndex();

            newValue = adjustNewValue(newValue, oldValue, sizeList);

            int extent = getExtent(newValue);
            int maxIndex;
            maxIndex = Math.max(lastIndex, extent) + 1;
            maxIndex = Math.max(newValue + extent, maxIndex);

            // TODO ALEX_SEP 不明白这段代码的必要性
//                //Check GridSelection.
//                GridSelection gridSelection = reportPane.getGridSelection();
//                int type = gridSelection.getType();
//                if(type == GridSelection.CELL) {
//                	Rectangle cellRectangle = gridSelection.getFirstCellRectangle();
//                	maxIndex = Math.max(cellRectangle.y + cellRectangle.height + 1, maxIndex);	//denny:- this.getReportPane().getFrozenRow()
//                }

            //Check old value.
            BoundedRangeModel boundedRangeModel = DynamicScrollBar.this.getModel();
            if (newValue != boundedRangeModel.getValue() ||
                    extent != boundedRangeModel.getExtent() ||
                    maxIndex != boundedRangeModel.getMaximum()) {
                boundedRangeModel.setRangeProperties(newValue, extent, 0, maxIndex, true);

                resetBeginValue(newValue);
                resetExtentValue(extent);
                DynamicScrollBar.this.reportPane.repaint(40);

                if (accessibleContext != null) {
                    accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY,
                            new Integer(oldValue),
                            new Integer(DynamicScrollBar.this.getValue()));
                }
            }
        }
    }

    private DynamicScrollBarHelper verticalBarHelper = new DynamicScrollBarHelper() {
        @Override
        public int getLastIndex() {
            return reportPane.getEditingElementCase().getRowCount() - 1;
        }

        @Override
        public DynamicUnitList getSizeList() {
            return ReportHelper.getRowHeightList(reportPane.getEditingElementCase());
        }

        @Override
        public double getVisibleSize() {
            return reportPane.getGrid().getHeight();
        }

        ;

        @Override
        public void resetBeginValue(int newValue) {
            DynamicScrollBar.this.reportPane.getGrid().setVerticalValue(newValue);
        }

        @Override
        public void resetExtentValue(int extentValue) {
            DynamicScrollBar.this.reportPane.getGrid().setVerticalExtent(extentValue);
        }
    };

    private DynamicScrollBarHelper horizontalBarHelper = new DynamicScrollBarHelper() {
        @Override
        public int getLastIndex() {
            return reportPane.getEditingElementCase().getColumnCount() - 1;
        }

        @Override
        public DynamicUnitList getSizeList() {
            return ReportHelper.getColumnWidthList(reportPane.getEditingElementCase());
        }

        @Override
        public double getVisibleSize() {
            return reportPane.getGrid().getWidth();
        }

        ;

        @Override
        public void resetBeginValue(int newValue) {
            DynamicScrollBar.this.reportPane.getGrid().setHorizontalValue(newValue);
        }

        @Override
        public void resetExtentValue(int extentValue) {
            DynamicScrollBar.this.reportPane.getGrid().setHorizontalExtent(extentValue);
        }
    };

    public boolean isSupportHide() {
        return isSupportHide;
    }

    public void setSupportHide(boolean supportHide) {
        isSupportHide = supportHide;
    }

    /**
     * Return the preferred size.
     */
    @Override
    public Dimension getPreferredSize() {
        if (isSupportHide) {
            if (this.getOrientation() == Adjustable.HORIZONTAL) {
                if (reportPane != null && !reportPane.isHorizontalScrollBarVisible()) {
                    return new Dimension(0, 0);
                }
            } else if (this.getOrientation() == Adjustable.VERTICAL) {
                if (reportPane != null && !reportPane.isVerticalScrollBarVisible()) {
                    return new Dimension(0, 0);
                }
            }
        }

        return super.getPreferredSize();
    }
}