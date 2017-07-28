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
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.utils.gui.GUICoreUtils;
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
	private JPanel layoutPane;
	private JPanel basicPane;
	private JPanel seniorPane;

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


	public static void main(String[] args){
//		JFrame jf = new JFrame("test");
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JPanel content = (JPanel) jf.getContentPane();
//		content.setLayout(new BorderLayout());
//		content.add(new CellExpandAttrPane().layoutPane(), BorderLayout.CENTER);
//		GUICoreUtils.centerWindow(jf);
//		jf.setSize(290, 400);
//		jf.setVisible(true);
	}

	private void initAllNames() {
		expandDirectionButton.setGlobalName(Inter.getLocText("FR-Designer_ExpandD_Expand_Direction"));
		leftFatherPane.setGlobalName(Inter.getLocText("FR-Designer_LeftParent"));
		rightFatherPane.setGlobalName(Inter.getLocText("FR-Designer_ExpandD_Up_Father_Cell"));
		horizontalExpandableCheckBox.setGlobalName(Inter.getLocText("FR-Designer_ExpandD_Expandable"));
		verticalExpandableCheckBox.setGlobalName(Inter.getLocText("FR-Designer_ExpandD_Expandable"));
	}

	private JPanel layoutPane() {
		layoutPane = new JPanel(new BorderLayout());
		basicPane = new JPanel();
		seniorPane = new JPanel();
		basicPane = new UIExpandablePane(Inter.getLocText("FR-Designer_Basic"),290,20,basicPane());
		seniorPane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"),290,20,seniorPane());
		layoutPane.add(basicPane,BorderLayout.NORTH);
		layoutPane.add(seniorPane,BorderLayout.CENTER);
		return layoutPane;
	}

	private JPanel basicPane(){
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{null,null},
				new Component[]{new UILabel(" "+Inter.getLocText("FR-Designer_ExpandD_Expand_Direction")+"   ", SwingConstants.LEFT), expandDirectionButton},
				new Component[]{new UILabel(" "+Inter.getLocText("FR-Designer_LeftParent"), SwingConstants.LEFT), leftFatherPane},
				new Component[]{new UILabel(" "+Inter.getLocText("FR-Designer_ExpandD_Up_Father_Cell"), SwingConstants.LEFT), rightFatherPane},
		};
		double[] rowSize = {p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}, {1, 3}, {1, 3}};
		return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
	}

	private JPanel seniorPane() {
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{null,null},
				new Component[]{horizontalExpandableCheckBox, null},
				new Component[]{verticalExpandableCheckBox, null},
				new Component[]{new UILabel(" "+Inter.getLocText("FR-Designer_ExpendSort"), SwingConstants.RIGHT), sortAfterExpand},
		};
		double[] rowSize = {p, p, p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1}, {1, 1}, {1, 3}, {1, 3}};
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
//		return "com/fr/design/images/expand/cellAttr.gif";
		return Inter.getLocText("FR-Designer_Expand");
	}


	@Override
	public void updateBean(TemplateCellElement cellElement) {
		CellExpandAttr cellExpandAttr = cellElement.getCellExpandAttr();
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
			cellElement.setCellExpandAttr(cellExpandAttr);
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_ExpandD_Expand_Direction"))) {
			cellExpandAttr.setDirection(expandDirectionButton.getSelectedItem());
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_LeftParent"))) {
			this.leftFatherPane.update(cellExpandAttr);
		}
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_ExpandD_Up_Father_Cell"))) {
			this.rightFatherPane.update(cellExpandAttr);
		}


		// extendable
		if (ComparatorUtils.equals(getGlobalName(), Inter.getLocText("FR-Designer_ExpandD-Expandable"))) {
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