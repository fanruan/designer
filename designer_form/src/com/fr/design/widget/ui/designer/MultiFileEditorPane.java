package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.MultiFileEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class MultiFileEditorPane extends FieldEditorDefinePane<MultiFileEditor> {
	private DictionaryComboBox acceptType;
	private UICheckBox singleFileCheckBox;
	private UINumberField fileSizeField;
	private UISpinner fontSizeField;

	public MultiFileEditorPane(XCreator xCreator) {
		super(xCreator);
	}

	
	@Override
	public String title4PopupWindow() {
		return "file";
	}

	@Override
	protected JPanel setFirstContentPane() {
		acceptType = new DictionaryComboBox(DictionaryConstants.acceptTypes, DictionaryConstants.fileTypeDisplays);
		singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD"));
		singleFileCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		fileSizeField = new UINumberField();
		fontSizeField = new UISpinner(0, 20, 1, 0);

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
				new Component[]{new UILabel(Inter.getLocText("File-Allow_Upload_Files") + ":"), acceptType},
				new Component[]{new UILabel( Inter.getLocText("File-File_Size_Limit") + ":"), fileSizeField},
				new Component[]{new UILabel( Inter.getLocText("FR-Designer_Font-Size")), fontSizeField}
		};
		double[] rowSize = {p, p,p,p};
		double[] columnSize = {p,f};
		int[][] rowCount = {{1, 1},{1, 1},{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		boundsPane.add(panel);

		return boundsPane;
	}

	@Override
	protected void populateSubFieldEditorBean(MultiFileEditor e) {
		// 这里存在兼容问题 getAccept可能没在待选项目中
		acceptType.setSelectedItem(e.getAccept());
		singleFileCheckBox.setSelected(e.isSingleFile());
		fileSizeField.setValue(e.getMaxSize());
		fontSizeField.setValue(e.getFontSize());
	}

	@Override
	protected MultiFileEditor updateSubFieldEditorBean() {
		MultiFileEditor ob = new MultiFileEditor();
		ob.setAccept((String) acceptType.getSelectedItem());
		ob.setSingleFile(singleFileCheckBox.isSelected());
		ob.setMaxSize(fileSizeField.getValue());
		ob.setFontSize((int)fontSizeField.getValue());
		return ob;
	}

}