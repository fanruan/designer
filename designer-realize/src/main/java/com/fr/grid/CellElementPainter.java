package com.fr.grid;

import java.awt.*;

import com.fr.base.Style;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.PaintUtils;
import com.fr.report.elementcase.ElementCase;

public class CellElementPainter {
	public void paintBackground(Graphics2D g2d, ElementCase report, CellElement ce, double width, double height) {
		Style.paintBackground(g2d, ce.getStyle(), width, height);
	}

	public void paintContent(Graphics2D g2d, ElementCase report, TemplateCellElement ce, int width, int height, int resolution) {
		PaintUtils.paintGridCellContent(g2d, ce, width, height, resolution);
	}

	public void paintBorder(Graphics2D g2d, ElementCase report, CellElement ce, double width, double height) {
		Style.paintBorder(g2d, ce.getStyle(), width, height);
	}
}