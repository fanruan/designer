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
import com.fr.design.layout.FRLeftFlowLayout;
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
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		JPanel centerPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
//		centerPane.add(singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD")));



		singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD"));
		acceptType = new DictionaryComboBox(DictionaryConstants.acceptTypes, DictionaryConstants.fileTypeDisplays);
		acceptType.setPreferredSize(new Dimension(100, 18));
		fileSizeField = new UINumberField();
		fileSizeField.setPreferredSize(new Dimension(80, 18));

		JPanel singleFilePane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		singleFilePane.add(singleFileCheckBox);
		centerPane.add(singleFilePane);

		JPanel allowTypePane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		allowTypePane.add(new UILabel(Inter.getLocText("File-Allow_Upload_Files") + ":"));
		allowTypePane.add(acceptType);
		centerPane.add(allowTypePane);

		JPanel fileSizePane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		fileSizePane.add(new UILabel(Inter.getLocText("File-File_Size_Limit") + ":"));
		fileSizePane.add(fileSizeField);
		fileSizePane.add(new UILabel(" KB"));
		centerPane.add(fileSizePane);

		contenter.add(centerPane);
		return contenter;
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