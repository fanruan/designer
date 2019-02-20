/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.clipboard;

import com.fr.grid.selection.CellSelection;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.CellElementComparator;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The clip of CellElement.
 */
public class CellElementsClip implements Cloneable, java.io.Serializable {
    private int columnSpan = 0;
    private int rowSpan = 0;
    private  FU[] columnWidth;
    private  FU[] rowHeight;
    private TemplateCellElement[] clips;

    public CellElementsClip(int columnSpan, int rowSpan, FU[] columnWidth , FU[] rowHeight, TemplateCellElement[] clips) {
    	this.columnSpan = columnSpan;
    	this.rowSpan = rowSpan;
    	this.columnWidth = columnWidth ;
		this.rowHeight = rowHeight;
    	this.clips = clips;
    }

    public CellElementsClip(int columnSpan, int rowSpan, TemplateCellElement[] clips) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.clips = clips;
    }

	public int getColumnSpan() {
		return columnSpan;
	}

	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public FU[] getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(FU[] columnWidth) {
		this.columnWidth = columnWidth;
	}

	public FU[] getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(FU[] rowHeight) {
		this.rowHeight = rowHeight;
	}

	public TemplateCellElement[] getClips() {
		return clips;
	}

	public void setClips(TemplateCellElement[] clips) {
		this.clips = clips;
	}

	public String compateExcelPaste() {
    	Arrays.sort(this.clips, CellElementComparator.getRowFirstComparator());

		// 排序
		StringBuffer sbuf = new StringBuffer();

		int currentRow = -1;
		for (int i = 0; i < clips.length; i++) {
			CellElement cellElement = clips[i];
			if (currentRow == -1) {// 初始化当前行.
				currentRow = cellElement.getRow();
			}

			if (currentRow < cellElement.getRow()) {
				for (int r = currentRow; r < cellElement.getRow(); r++) {
					sbuf.append('\n');
				}
				currentRow = cellElement.getRow();
			}

			// 添加分隔符号.
			if (sbuf.length() > 0 && sbuf.charAt(sbuf.length() - 1) != '\n') {
				sbuf.append('\t');
			}
			//REPORT-5134:会复制出null
			if (cellElement.getValue() == null) {
				sbuf.append(StringUtils.EMPTY);
			} else {
				sbuf.append(cellElement.getValue());
			}
		}

		return sbuf.toString();
    }

    public CellSelection pasteAt(TemplateElementCase ec, int column, int row) {

    	Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
		while (cells.hasNext()) {
			TemplateCellElement cellElement = (TemplateCellElement)cells.next();
			ec.removeCellElement(cellElement);
		}
    	for (int i = 0; i < clips.length; i++) {
    		TemplateCellElement cellElement;
    		try {
    			cellElement = (TemplateCellElement) clips[i].clone();
    		} catch (CloneNotSupportedException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
    			return null;
    		}

    		// peter:因为前面已经将这个位置的元素删除了,所以不需要override了.
    		ec.addCellElement((TemplateCellElement) cellElement.deriveCellElement(
    			column + cellElement.getColumn(), row + cellElement.getRow()
    		), false);
    	}
    	//设置单元格的宽高
        if(this.columnWidth != null && this.rowHeight != null){
            pasteWidthAndHeight(ec, column, row, columnSpan, rowSpan);
        }
    	return new CellSelection(column, row, columnSpan, rowSpan);
    }

    public  void pasteWidthAndHeight(TemplateElementCase ec, int column, int row, int columnSpan, int rowSpan){
			for(int i = 0; i < columnSpan; i++){
				ec.setColumnWidth(column + i, columnWidth[i]);
			}
			for(int j = 0; j < rowSpan; j++){
				ec.setRowHeight(row + j, rowHeight[j]);
			}
	}

    public void pasteAtRegion(TemplateElementCase ec,
    		int startColumn, int startRow,
    		int column, int row,
            int columnSpan, int rowSpan) {
        for (int i = 0; i < clips.length; i++) {
            TemplateCellElement cellElement = clips[i];

            cellElement = (TemplateCellElement) cellElement.deriveCellElement(startColumn + cellElement.getColumn(), startRow + cellElement.getRow());
            //peter:检查是否越界,越界就不做了.
            if (cellElement.getColumn() >= column + columnSpan || cellElement.getRow() >= row + rowSpan || cellElement.getColumn() < column
                    || cellElement.getRow() < row) {
                continue;
            }

            ec.addCellElement(cellElement);
        }
    }

    /**
     * Clone.
     */
    @Override
	public Object clone() throws CloneNotSupportedException {
        CellElementsClip cloned = (CellElementsClip) super.clone();

        if (this.clips != null) {
        	cloned.clips = new TemplateCellElement[this.clips.length];
        	for (int i = 0; i < this.clips.length; i++) {
        		cloned.clips[i] = (TemplateCellElement)this.clips[i].clone();
        	}
        }

        return cloned;
    }
}