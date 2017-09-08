package com.fr.grid;

import java.awt.Rectangle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.script.FunctionHelper;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;

/**
 * Kevin Wang: inner class used by method intelliElements iterating in GridUtils.java.
 */
public class IntelliElements {
    //fields
    public static final int DIRECTION_UNDEF = -1;
    public static final int DIRECTION_UP_TO_DOWN = 0;
    public static final int DIRECTION_DOWN_TO_UP = 1;
    public static final int DIRECTION_LEFT_TO_RIGHT = 2;
    public static final int DIRECTION_RIGHT_TO_LEFT = 3;

    public static final int ACTION_SEQUENCING = 0; //default
    public static final int ACTION_REPLICATION = 1; // replicate/copy the elements

    // The following two const are only used in method generateSimpleFormula
    public static final int FORMULA_NONE_PARA_SAME = -1;
    public static final int FORMULA_HOR_PARA_SAME = 0;
    public static final int FORMULA_VER_PARA_SAME = 1;

    private int direction = DIRECTION_UNDEF; // drag direction
    private int action = ACTION_SEQUENCING;//default
    private boolean isStyleSupported = true; //default

    //arguments passed in as members
    private ElementCasePane reportPane;
    private TemplateElementCase report;

    private Rectangle oldCellRectangle = null;
    private Rectangle dragCellRectangle = null;


    /**
     * Intelligent response to user's dragging right bottom of a cell selection region
     */
    public static void iterating(ElementCasePane reportPane, Rectangle oldCellRectangle, Rectangle dragCellRectangle) {
        IntelliElements intelliElements = new IntelliElements(reportPane, oldCellRectangle, dragCellRectangle);
        //set options
        intelliElements.setAction(IntelliElements.ACTION_SEQUENCING);
        intelliElements.setStyleSupported(true);

        //do intelligent action now
        intelliElements.doIntelliAction();
    }

    /**
     * Constructor
     *
     * @param reportPane        ElementCasePane object
     */
    public IntelliElements(ElementCasePane reportPane, Rectangle oldCellRectangle, Rectangle dragCellRectangle) {
        this.reportPane = reportPane;
        this.report = reportPane.getEditingElementCase();

        this.oldCellRectangle = oldCellRectangle;
        this.dragCellRectangle = dragCellRectangle;

    }

    /**
     * Specify the action when this drag operation
     *
     * @param action either IntelliElements.ACTION_REPLICATION or
     *               IntelliElements.ACTION_SEQUENCING, presently
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Specify if copy/sequence both content and style, or simply do it for content only
     *
     * @param isStyleSupported specify if support content only. Default is true.
     */
    public void setStyleSupported(boolean isStyleSupported) {
        this.isStyleSupported = isStyleSupported;
    }

    /**
     * Auto-generating elements in the drag region according to those in the old region
     * This is one of the few public methods of this inner class
     */
    public void doIntelliAction() {
        // just do it!
        analyzeDirection();

        // Assumption: elements in old region are of the same type
        if (this.action == IntelliElements.ACTION_REPLICATION) {
            doReplication();
            return;
        }

        if (this.direction == IntelliElements.DIRECTION_UP_TO_DOWN) {
            U2DDHelper.doIntelliAction();
        } else if (this.direction == IntelliElements.DIRECTION_DOWN_TO_UP) {
            D2UDHelper.doIntelliAction();
        } else if (this.direction == IntelliElements.DIRECTION_LEFT_TO_RIGHT) {
            L2RDHelper.doIntelliAction();
        } else if (this.direction == IntelliElements.DIRECTION_RIGHT_TO_LEFT) {
            R2LDHelper.doIntelliAction();
        }

        //设置GridSelection.
        reportPane.setSelection(new CellSelection(this.dragCellRectangle.x, this.dragCellRectangle.y, this.dragCellRectangle.width, this.dragCellRectangle.height));
        reportPane.repaint();
    }

    /**
     * Hmm..., I copied some of Peter's codes from former method
     * doMouseReleased in Grid.java
     */
    private void doReplication() {
        if (this.direction == IntelliElements.DIRECTION_UP_TO_DOWN) {
            U2DDHelper.replicate();
        } else if (this.direction == IntelliElements.DIRECTION_DOWN_TO_UP) {
            D2UDHelper.replicate();
        } else if (this.direction == IntelliElements.DIRECTION_LEFT_TO_RIGHT) {
            L2RDHelper.replicate();
        } else if (this.direction == IntelliElements.DIRECTION_RIGHT_TO_LEFT) {
            R2LDHelper.replicate();
        }
    }

    private abstract class DragHelper {
        protected abstract boolean havetoModify();

        protected abstract void copy(CellElementsClip cellElementsClip);


        public abstract int getStart();

        public abstract int getEnd();

        public abstract int getStep();

        public abstract int[] getRect(int i);

        public void replicate() {
            if (havetoModify()) {
                ElementsTransferable elementsTransferable = GridUtils.caculateElementsTransferable(reportPane);

                CellElementsClip cellElementsClip = null;
                Object firstObject = elementsTransferable.getFirstObject();
                if (firstObject != null) {
                    if (firstObject instanceof CellElementsClip) {
                        cellElementsClip = (CellElementsClip) firstObject;
                    }
                }

                //cellElementsCopy
                if (cellElementsClip != null) {
                    copy(cellElementsClip);
                }

                //设置GridSelection.
                reportPane.setSelection(new CellSelection(
                        IntelliElements.this.dragCellRectangle.x,
                        IntelliElements.this.dragCellRectangle.y,
                        IntelliElements.this.dragCellRectangle.width,
                        IntelliElements.this.dragCellRectangle.height
                ));
            }

        }

        public void doIntelliAction() {
            for (int colIndex = getStartColumnIndex(), colEnd = getEndColumnIndex(); colIndex < colEnd; colIndex++) {
                for (int rowIndex = getStartRowIndex(), rowEnd = getEndRowIndex(); rowIndex < rowEnd; rowIndex++) {
                    TemplateCellElement sourceCellElement = getSourceCellElementByColumnRow(colIndex, rowIndex);

                    if (sourceCellElement == null) {
                        sourceCellElement = new DefaultTemplateCellElement();
                    }
                    TemplateCellElement newCellElement = new DefaultTemplateCellElement(colIndex, rowIndex);
                    applyStyle(newCellElement, sourceCellElement);//style
                    if (sourceCellElement.getValue() instanceof DSColumn) {
                        DSColumn dsColumn = (DSColumn) sourceCellElement.getValue();
                        newCellElement.setValue(dsColumn);
                        newCellElement.setCellExpandAttr(sourceCellElement.getCellExpandAttr());
                    } else if (sourceCellElement.getValue() instanceof Number) {
                        newCellElement.setValue(processNumber((Number) sourceCellElement.getValue()));
                    } else if (sourceCellElement.getValue() instanceof Formula) {
                        Formula formula = (Formula) sourceCellElement.getValue();
                        formula = this.generateSimpleFormula(formula, 1);
                        newCellElement.setValue(formula);
                    } else {
                        try {
                            //richer:不改变原单元格
                            newCellElement.setValue(BaseUtils.cloneObject(sourceCellElement.getValue()));
                        } catch (CloneNotSupportedException e) {
                            FRContext.getLogger().error(e.getMessage(), e);
                        }
                    }

                    report.addCellElement(newCellElement);
                }
            }
        }

        protected abstract int getStartColumnIndex();

        protected abstract int getEndColumnIndex();

        protected abstract int getStartRowIndex();

        protected abstract int getEndRowIndex();

        protected abstract TemplateCellElement getSourceCellElementByColumnRow(int columnIndex, int rowIndex);

        protected abstract Number processNumber(Number i);

        protected abstract ColumnRow processColumnRow(ColumnRow org, int diff);

        private Formula generateSimpleFormula(Formula formula, int diff) {
            Formula newFormula;
            try {
                newFormula = (Formula) (formula.clone());
            } catch (CloneNotSupportedException e) {
                newFormula = new Formula();
                FRContext.getLogger().error(e.getMessage(), e);
            }
            String formulaContent = formula.getContent();
            StringBuffer newFormulaContent = new StringBuffer();

            String colRowRegex = "[a-z|A-Z]+[0-9]+";
            Pattern pattern = Pattern.compile(colRowRegex);
            Matcher matcher = pattern.matcher(formulaContent);

            int start = 0;
            while (matcher.find()) {
                int tokenStart = matcher.start();
                int tokenEnd = matcher.end();

                //jack 这个地方就是为了让参数的形式不扩展。
                char isParam = formulaContent.charAt(tokenStart - 1);
                if (isParam == '$') {
                    continue;
                }

                String colRow = formulaContent.substring(tokenStart, tokenEnd);

                ColumnRow newCR = processColumnRow(BaseUtils.convertCellStringToColumnRow(colRow), diff);
                String newColRow = BaseUtils.convertColumnRowToCellString(newCR);

                newFormulaContent.append(formulaContent.substring(start, tokenStart));
                newFormulaContent.append(newColRow);

                start = tokenEnd;
            }
            newFormulaContent.append(formulaContent.substring(start, formulaContent.length()));
            newFormula.setContent(newFormulaContent.toString());
            return newFormula;
        }
    }

    // 顺时针的拖拽，包括从左到右和从上到下
    private abstract class ClockwiseDragHelper extends DragHelper {
        @Override
		public void copy(CellElementsClip cellElementsClip) {
            for (int i = getStart(); i < getEnd(); i += getStep()) {
                int[] rect = getRect(i);
                
                cellElementsClip.pasteAtRegion(reportPane.getEditingElementCase(),
                        rect[0], rect[1], rect[2], rect[3], rect[4], rect[5]);
            }
        }

    }

    // 逆时针的拖拽，包括从右到左和从下到上
    private abstract class CounterClockwiseDragHelper extends DragHelper {
        @Override
		public void copy(CellElementsClip cellElementsClip) {
            for (int i = getStart(); i > getEnd(); i -= getStep()) {
                int[] rect = getRect(i);
                
                cellElementsClip.pasteAtRegion(reportPane.getEditingElementCase(), rect[0], rect[1], rect[2], rect[3], rect[4], rect[5]);
            }
        }
    }

    private DragHelper L2RDHelper = new ClockwiseDragHelper() {
        @Override
        public int getStart() {
            return oldCellRectangle.x + oldCellRectangle.width;
        }

        @Override
        public int getEnd() {
            return IntelliElements.this.dragCellRectangle.x + IntelliElements.this.dragCellRectangle.width;
        }

        @Override
		public int getStep() {
            return oldCellRectangle.width;
        }

        @Override
        public int[] getRect(int i) {
            return new int[]{i, oldCellRectangle.y, i, oldCellRectangle.y,
                    //peter:最后不能越界.
                    Math.min(oldCellRectangle.width, IntelliElements.this.dragCellRectangle.x + IntelliElements.this.dragCellRectangle.width - i),
                    oldCellRectangle.height
            };
        }

        @Override
		public boolean havetoModify() {
            return IntelliElements.this.dragCellRectangle.width > IntelliElements.this.oldCellRectangle.width;
        }

        @Override
		public int getStartColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x + ((CellSelection)reportPane.getSelection()).getColumnSpan();
        }

        @Override
		public int getEndColumnIndex() {
            return IntelliElements.this.dragCellRectangle.x + IntelliElements.this.dragCellRectangle.width;
        }

        @Override
		public int getStartRowIndex() {
            return IntelliElements.this.oldCellRectangle.y;
        }

        @Override
		public int getEndRowIndex() {
            return IntelliElements.this.oldCellRectangle.y + IntelliElements.this.oldCellRectangle.height;
        }

        @Override
		public TemplateCellElement getSourceCellElementByColumnRow(int columnIndex, int rowIndex) {
            return report.getTemplateCellElement(columnIndex - IntelliElements.this.oldCellRectangle.width, rowIndex);
        }

        @Override
		protected Number processNumber(Number i) {
            return FunctionHelper.asNumber(i.doubleValue() + 1);
        }

        @Override
		protected ColumnRow processColumnRow(ColumnRow org, int diff) {
            return ColumnRow.valueOf(org.column + diff, org.row);
        }

    };

    private DragHelper R2LDHelper = new CounterClockwiseDragHelper() {
        @Override
        public int getStart() {
            return oldCellRectangle.x - oldCellRectangle.width;
        }

        @Override
        public int getEnd() {
            return IntelliElements.this.dragCellRectangle.x - oldCellRectangle.width;
        }

        @Override
        public int getStep() {
            return oldCellRectangle.width;
        }

        @Override
        public int[] getRect(int i) {
            return new int[]{
                    i,
                    oldCellRectangle.y,
                    Math.max(i, IntelliElements.this.dragCellRectangle.x), oldCellRectangle.y,
                    //peter:最前面的时候不能越界
                    Math.min(oldCellRectangle.width, oldCellRectangle.width - (IntelliElements.this.dragCellRectangle.x - i)), oldCellRectangle.height
            };
        }

        @Override
		public boolean havetoModify() {
            return true;
        }

        @Override
		public int getStartRowIndex() {
            return IntelliElements.this.oldCellRectangle.y;
        }

        @Override
		public int getEndRowIndex() {
            return IntelliElements.this.oldCellRectangle.y + IntelliElements.this.oldCellRectangle.height;
        }

        @Override
		public int getStartColumnIndex() {
            return IntelliElements.this.dragCellRectangle.x;
        }

        @Override
		public int getEndColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x;
        }

        @Override
		public TemplateCellElement getSourceCellElementByColumnRow(int columnIndex, int rowIndex) {
            return report.getTemplateCellElement(IntelliElements.this.oldCellRectangle.x + (columnIndex - IntelliElements.this.dragCellRectangle.x) % (IntelliElements.this.oldCellRectangle.width), rowIndex);
        }

        @Override
		protected Number processNumber(Number i) {
            return i;
        }

        @Override
		protected ColumnRow processColumnRow(ColumnRow org, int diff) {
            return ColumnRow.valueOf(Math.max(0, org.column - diff), org.row);
        }
    };

    private DragHelper U2DDHelper = new ClockwiseDragHelper() {

        @Override
        public int getStart() {
            return oldCellRectangle.y + oldCellRectangle.height;
        }

        @Override
        public int getEnd() {
            return IntelliElements.this.dragCellRectangle.y + IntelliElements.this.dragCellRectangle.height;
        }

        @Override
        public int getStep() {
            return oldCellRectangle.height;
        }

        @Override
        public int[] getRect(int i) {
            return new int[]{
                    oldCellRectangle.x, i, oldCellRectangle.x, i,
                    oldCellRectangle.width,
                    //peter:最后不能越界.
                    Math.min(oldCellRectangle.height, IntelliElements.this.dragCellRectangle.y + IntelliElements.this.dragCellRectangle.height - i)
            };
        }

        @Override
		public boolean havetoModify() {
            return IntelliElements.this.dragCellRectangle.height > IntelliElements.this.oldCellRectangle.height;
        }

        @Override
		public int getStartColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x;
        }

        @Override
		public int getEndColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x + IntelliElements.this.oldCellRectangle.width;
        }

        @Override
		public int getStartRowIndex() {
            return IntelliElements.this.oldCellRectangle.y + ((CellSelection)reportPane.getSelection()).getRowSpan();
        }

        @Override
		public int getEndRowIndex() {
            return IntelliElements.this.dragCellRectangle.y + IntelliElements.this.dragCellRectangle.height;
        }

        @Override
		public TemplateCellElement getSourceCellElementByColumnRow(int columnIndex, int rowIndex) {
            return report.getTemplateCellElement(columnIndex, rowIndex - IntelliElements.this.oldCellRectangle.height);
        }

        @Override
		protected Number processNumber(Number i) {
            return FunctionHelper.asNumber(i.doubleValue() + 1);
        }

        @Override
		protected ColumnRow processColumnRow(ColumnRow org, int diff) {
            return ColumnRow.valueOf(org.column, org.row + diff);
        }
    };

    private DragHelper D2UDHelper = new CounterClockwiseDragHelper() {
        @Override
        public int getStart() {
            return oldCellRectangle.y - oldCellRectangle.height;
        }

        @Override
        public int getEnd() {
            return IntelliElements.this.dragCellRectangle.y - oldCellRectangle.height;
        }

        @Override
        public int getStep() {
            return oldCellRectangle.height;
        }

        @Override
        public int[] getRect(int i) {
            return new int[]{
                    oldCellRectangle.x, i, oldCellRectangle.x,
                    Math.max(i, IntelliElements.this.dragCellRectangle.y), oldCellRectangle.width,
                    //peter:最前面的时候不能越界
                    Math.min(oldCellRectangle.height, oldCellRectangle.height - (IntelliElements.this.dragCellRectangle.y - i))

            };
        }

        @Override
		public boolean havetoModify() {
            return true;
        }

        @Override
		public int getStartRowIndex() {
            return IntelliElements.this.dragCellRectangle.y;
        }

        @Override
		public int getEndRowIndex() {
            return IntelliElements.this.oldCellRectangle.y;
        }

        @Override
		public int getStartColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x;
        }

        @Override
		public int getEndColumnIndex() {
            return IntelliElements.this.oldCellRectangle.x + IntelliElements.this.oldCellRectangle.width;
        }

        @Override
		public TemplateCellElement getSourceCellElementByColumnRow(int columnIndex, int rowIndex) {
            return report.getTemplateCellElement(columnIndex, IntelliElements.this.oldCellRectangle.y + (rowIndex - IntelliElements.this.dragCellRectangle.y) % (IntelliElements.this.oldCellRectangle.height));
        }

        @Override
		protected Number processNumber(Number i) {
            return i;
        }

        @Override
		protected ColumnRow processColumnRow(ColumnRow org, int diff) {
            return ColumnRow.valueOf(org.column, Math.max(0, org.row - diff));
        }
    };

    private void analyzeDirection() {
        //vertical
        if (this.dragCellRectangle.x == oldCellRectangle.x && this.dragCellRectangle.width == oldCellRectangle.width) {
            if (this.dragCellRectangle.y == oldCellRectangle.y) {
                this.direction = IntelliElements.DIRECTION_UP_TO_DOWN;
            } else if (this.dragCellRectangle.y < oldCellRectangle.y) {
                this.direction = IntelliElements.DIRECTION_DOWN_TO_UP;
            }
        }
        //horizontal
        else if (this.dragCellRectangle.y == oldCellRectangle.y && this.dragCellRectangle.height == oldCellRectangle.height) {
            if (this.dragCellRectangle.x == oldCellRectangle.x) {
                this.direction = IntelliElements.DIRECTION_LEFT_TO_RIGHT;
            } else if (this.dragCellRectangle.x < oldCellRectangle.x) {
                this.direction = IntelliElements.DIRECTION_RIGHT_TO_LEFT;
            }
        }
    }

    /**
     * Apply style for each element
     *
     * @param newCellElement A new CellElement object
     * @param oldCellElement A reference CellElement object. Its style be cloned and
     *                       then used by the new one.
     */
    private void applyStyle(CellElement newCellElement, CellElement oldCellElement) {
        if (this.isStyleSupported) {
            // must clone, but not simply use the other's style
            newCellElement.setStyle(oldCellElement.getStyle());
        }
        // else: simply use the default style assigned when the object is created
    }
}