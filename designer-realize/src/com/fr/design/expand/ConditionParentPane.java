package com.fr.design.expand;


import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.design.utils.gui.GUICoreUtils;

public class ConditionParentPane extends JPanel {
	private ParentPane leftParentPane;
	private ParentPane upParentPane;

	public ConditionParentPane() {
		this.initComponents(null);
	}

	public ConditionParentPane(ActionListener listener) {
		this.initComponents(listener);
	}


	public void initComponents(ActionListener listener) {
		JPanel innerthis=FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
    	this.add(innerthis);


		JPanel eastPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		innerthis.add(eastPane);

		eastPane.add(GUICoreUtils.createFlowPane(new JComponent[] {
				new UILabel(Inter.getLocText("LeftParent") + ": "),
				leftParentPane = new ParentPane(ParentPane.LEFT, listener) }, FlowLayout.LEFT));

		eastPane.add(GUICoreUtils.createFlowPane(
				new JComponent[] {
						new UILabel(Inter.getLocText("UpParent") + ": "),
						upParentPane = new ParentPane(ParentPane.UP, listener) }, FlowLayout.LEFT));
	}
	public void putElementcase(ElementCasePane t){
		leftParentPane.putElementcase(t);
		upParentPane.putElementcase(t);
	}
	public void putCellElement(TemplateCellElement cellElement){
		leftParentPane.putCellElement(cellElement);
		upParentPane.putCellElement(cellElement);
	}

	public void populate(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}
		
		this.leftParentPane.populate(cellExpandAttr);
		this.upParentPane.populate(cellExpandAttr);
	}

	public void update(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}

		this.leftParentPane.update(cellExpandAttr);
		this.upParentPane.update(cellExpandAttr);
	}
}