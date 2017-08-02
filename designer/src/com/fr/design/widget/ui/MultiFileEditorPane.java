package com.fr.design.widget.ui;

import java.awt.*;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.MultiFileEditor;
import com.fr.general.Inter;

public class MultiFileEditorPane extends FieldEditorDefinePane<MultiFileEditor> {
	private DictionaryComboBox acceptType;
	private UICheckBox singleFileCheckBox;
	private UINumberField fileSizeField;

	public MultiFileEditorPane() {
		this.initComponents();
	}

	
	@Override
	protected String title4PopupWindow() {
		return "file";
	}

	@Override
	protected JPanel setFirstContentPane() {
		acceptType = new DictionaryComboBox(DictionaryConstants.acceptTypes, DictionaryConstants.fileTypeDisplays);
		acceptType.setPreferredSize(new Dimension(200, 18));
		singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD"));
		fileSizeField = new UINumberField();
		fileSizeField.setPreferredSize(new Dimension(80, 18));


		JPanel singleFilePane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		singleFilePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		singleFilePane.add(singleFileCheckBox);

		JPanel allowTypePane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		allowTypePane.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		allowTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		allowTypePane.add(new UILabel("   " + Inter.getLocText("File-Allow_Upload_Files") + ":"));
		allowTypePane.add(acceptType);

		JPanel fileSizePane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		fileSizePane.add(new UILabel(" " + Inter.getLocText("File-File_Size_Limit") + ":"));
		fileSizePane.add(fileSizeField);
		fileSizePane.add(new UILabel(" KB"));

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{singleFileCheckBox, null },
				new Component[]{new UILabel("   " + Inter.getLocText("File-Allow_Upload_Files") + ":"), acceptType},
				new Component[]{new UILabel(" " + Inter.getLocText("File-File_Size_Limit") + ":"), fileSizeField},
		};
		double[] rowSize = {p, p,p};
		double[] columnSize = {p,f};
		int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, 5);


		return panel;
	}

	@Override
	protected void populateSubFieldEditorBean(MultiFileEditor e) {
		// 这里存在兼容问题 getAccept可能没在待选项目中
		acceptType.setSelectedItem(e.getAccept());
		singleFileCheckBox.setSelected(e.isSingleFile());
		fileSizeField.setValue(e.getMaxSize());
	}

	@Override
	protected MultiFileEditor updateSubFieldEditorBean() {
		MultiFileEditor ob = new MultiFileEditor();
		ob.setAccept((String) acceptType.getSelectedItem());
		ob.setSingleFile(singleFileCheckBox.isSelected());
		ob.setMaxSize(fileSizeField.getValue());
		return ob;
	}

}