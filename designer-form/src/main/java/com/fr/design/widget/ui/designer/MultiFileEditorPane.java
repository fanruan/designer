package com.fr.design.widget.ui.designer;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FontSizeComboPane;
import com.fr.form.ui.MultiFileEditor;


import javax.swing.*;
import java.awt.*;

public class MultiFileEditorPane extends FieldEditorDefinePane<MultiFileEditor> {
	private DictionaryComboBox acceptType;
	private UICheckBox singleFileCheckBox;
	private UISpinner fileSizeField;
	private FontSizeComboPane fontSizeField;

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
		singleFileCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("SINGLE_FILE_UPLOAD"));
		singleFileCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		fileSizeField = new UISpinner(0, Integer.MAX_VALUE, 1, -1);
		fileSizeField.setPreferredSize(new Dimension(140, 20));
		fontSizeField = new FontSizeComboPane();

		JPanel singleFilePane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		singleFilePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		singleFilePane.add(singleFileCheckBox);

		JPanel fileSizePane = new JPanel(new BorderLayout());
		fileSizePane.add(fileSizeField, BorderLayout.CENTER);
		fileSizePane.add(new UILabel(" KB"), BorderLayout.EAST);

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{singleFileCheckBox, null },
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_File_Allow_Upload_Files")), acceptType},
				new Component[]{new UILabel( com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_File_Size_Limit")), fileSizePane},
				new Component[]{new UILabel( com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Font-Size")), fontSizeField}
		};
		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p,f};
		int[][] rowCount = {{1, 1},{1, 1},{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
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
		MultiFileEditor ob = (MultiFileEditor)creator.toData();
		ob.setAccept((String) acceptType.getSelectedItem());
		ob.setSingleFile(singleFileCheckBox.isSelected());
		ob.setMaxSize(fileSizeField.getValue());
		ob.setFontSize(fontSizeField.getValue());
		return ob;
	}

}
