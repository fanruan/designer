package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.BaseUtils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.expand.ExpandLeftFatherPane;
import com.fr.design.expand.ExpandUpFatherPane;
import com.fr.design.expand.SortExpandAttrPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格扩展属性面板，是属性表面板的一个种类
 */
public class CellExpandAttrPane extends AbstractCellAttrPane {
	private UIButtonGroup<Byte> expandDirectionButton;
	private ExpandLeftFatherPane leftFatherPane;
	private ExpandUpFatherPane rightFatherPane;
	private UICheckBox horizontalExpandableCheckBox;
	private UICheckBox verticalExpandableCheckBox;
	private SortExpandAttrPane sortAfterExpand;

	/**
	 *
	 * @return
	 */
	public JPanel createContentPane() {
		String[] nameArray = {Inter.getLocText("ExpandD-Not_Expand"), Inter.getLocText("Utils-Top_to_Bottom"), Inter.getLocText("Utils-Left_to_Right")};
		Icon[] iconArray = {
				BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png"),
				BaseUtils.readIcon("/com/fr/design/images/expand/vertical.png"),
				BaseUtils.readIcon("/com/fr/design/images/expand/landspace.png")
		};
		Byte[] valueArray = {Constants.NONE, Constants.TOP_TO_BOTTOM, Constants.LEFT_TO_RIGHT};
		expandDirectionButton = new UIButtonGroup<Byte>(iconArray, valueArray);
		expandDirectionButton.setAllToolTips(nameArray);
		leftFatherPane = new ExpandLeftFatherPane();
		rightFatherPane = new ExpandUpFatherPane();
		horizontalExpandableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable"));
		verticalExpandableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable"));
		sortAfterExpand = new SortExpandAttrPane();
		initAllNames();
		return layoutPane();
	}


	private void initAllNames() {
		expandDirectionButton.setGlobalName(Inter.getLocText("ExpandD-Expand_Direction"));
		leftFatherPane.setGlobalName(Inter.getLocText("LeftParent"));
		rightFatherPane.setGlobalName(Inter.getLocText("ExpandD-Up_Father_Cell"));
		horizontalExpandableCheckBox.setGlobalName(Inter.getLocText("ExpandD-Expandable"));
		verticalExpandableCheckBox.setGlobalName(Inter.getLocText("ExpandD-Expandable"));
	}

	private JPanel layoutPane() {
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("ExpandD-Expand_Direction") + ":", SwingConstants.RIGHT), expandDirectionButton},
				new Component[]{new UILabel(Inter.getLocText("LeftParent") + ":", SwingConstants.RIGHT), leftFatherPane},
				new Component[]{new UILabel(Inter.getLocText("ExpandD-Up_Father_Cell") + ":", SwingConstants.RIGHT), rightFatherPane},
				new Component[]{new JSeparator(), null},
				new Component[]{new UILabel(Inter.getLocText("ExpandD-Expandable") + ":", SwingConstants.RIGHT), horizontalExpandableCheckBox},
				new Component[]{null, verticalExpandableCheckBox},
				new Component[]{new UILabel(Inter.getLocText("ExpandD-Sort_After_Expand") + ":", SwingConstants.RIGHT), sortAfterExpand},
		};
		double[] rowSize = {p, p, p, p, p, p, p, p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1}, {1, 3}, {1, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 3}};
		return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
	}


	@Override
	protected void populateBean() {
		this.leftFatherPane.setElementCasePane(elementCasePane);
		this.rightFatherPane.setElementCasePane(elementCasePane);
		CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
			cellElement.setCellExpandAttr(cellExpandAttr);
		}
		expandDirectionButton.setSelectedItem(cellExpandAttr.getDirection());
		this.leftFatherPane.populate(cellExpandAttr);
		this.rightFatherPane.populate(cellExpandAttr);
		switch (cellExpandAttr.getExtendable()) {
			case CellExpandAttr.Both_EXTENDABLE:
				horizontalExpandableCheckBox.setSelected(true);
				verticalExpandableCheckBox.setSelected(true);
				break;
			case CellExpandAttr.Vertical_EXTENDABLE:
				horizontalExpandableCheckBox.setSelected(false);
				verticalExpandableCheckBox.setSelected(true);
				break;
			case CellExpandAttr.Horizontal_EXTENDABLE:
				horizontalExpandableCheckBox.setSelected(true);
				verticalExpandableCheckBox.setSelected(false);
				break;
			default: {
				horizontalExpandableCheckBox.setSelected(false);
				verticalExpandableCheckBox.setSelected(false);
			}
		}

		sortAfterExpand.populate(cellExpandAttr);
	}


	@Override
	public String getIconPath() {
		return "com/fr/design/images/expand/cellAttr.gif";
	}

	@Override
	public void updateBean(TemplateCellElement cellElement) {
		CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
			cellElement.setCellExpandAttr(cellExpandAttr);
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Expand_Direction"))) {
			cellExpandAttr.setDirection(expandDirectionButton.getSelectedItem());
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("LeftParent"))) {
			this.leftFatherPane.update(cellExpandAttr);
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Up_Father_Cell"))) {
			this.rightFatherPane.update(cellExpandAttr);
		}


		// extendable
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Expandable"))) {
			if (horizontalExpandableCheckBox.isSelected()) {
				if (verticalExpandableCheckBox.isSelected()) {
					cellExpandAttr.setExtendable(CellExpandAttr.Both_EXTENDABLE);
				} else {
					cellExpandAttr.setExtendable(CellExpandAttr.Horizontal_EXTENDABLE);
				}
			} else {
				if (verticalExpandableCheckBox.isSelected()) {
					cellExpandAttr.setExtendable(CellExpandAttr.Vertical_EXTENDABLE);
				} else {
					cellExpandAttr.setExtendable(CellExpandAttr.None_EXTENDABLE);
				}
			}
		}

		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("ExpandD-Sort_After_Expand"))) {
			sortAfterExpand.update(cellExpandAttr);
		}

	}

	/**
	 *
	 */
	public void updateBeans() {
		TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
		int cellRectangleCount = cs.getCellRectangleCount();
		for (int rect = 0; rect < cellRectangleCount; rect++) {
			Rectangle cellRectangle = cs.getCellRectangle(rect);
			// 需要先行后列地增加新元素。
			for (int j = 0; j < cellRectangle.height; j++) {
				for (int i = 0; i < cellRectangle.width; i++) {
					int column = i + cellRectangle.x;
					int row = j + cellRectangle.y;
					TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
					if (cellElement == null) {
						cellElement = new DefaultTemplateCellElement(column, row);
						elementCase.addCellElement(cellElement);
					}
					updateBean(cellElement);
				}
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ExpandD-Expand_Attribute");
	}


}