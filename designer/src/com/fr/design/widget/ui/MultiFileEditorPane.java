package com.fr.design.widget.ui;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.MultiFileEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class MultiFileEditorPane extends FieldEditorDefinePane<MultiFileEditor> {
    private DictionaryComboBox acceptType;
    private UICheckBox singleFileCheckBox;
    private UISpinner fileSizeField;

    public MultiFileEditorPane() {
        this.initComponents();
    }


    @Override
    protected String title4PopupWindow() {
        return "file";
    }

    @Override
    protected JPanel setFirstContentPane() {
        JPanel contenter = new JPanel(new BorderLayout());

        singleFileCheckBox = new UICheckBox(Inter.getLocText("SINGLE_FILE_UPLOAD"));
        singleFileCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        acceptType = new DictionaryComboBox(DictionaryConstants.acceptTypes, DictionaryConstants.fileTypeDisplays);
//		acceptType.setPreferredSize(new Dimension(100, 20));
        fileSizeField = new UISpinner(0, Integer.MAX_VALUE, 1, -1);
        fileSizeField.setPreferredSize(new Dimension(140, 20));

        JPanel fileSizePane = new JPanel(new BorderLayout());
        UILabel fileTypeLabel = new UILabel(Inter.getLocText("Utils-File_type"));
        UILabel fileSizeLabel = new UILabel(Inter.getLocText("FR-Designer_Size_Limit"));
        fileSizePane.add(fileSizeField, BorderLayout.CENTER);
        fileSizePane.add(new UILabel(" KB"), BorderLayout.EAST);

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{singleFileCheckBox, null},
                new Component[]{fileTypeLabel, acceptType},
                new Component[]{fileSizeLabel, fileSizePane},
        };
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        contenter.add(panel, BorderLayout.CENTER);

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